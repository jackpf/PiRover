#include "gpio.hpp"

bool GPIO::isSetup = false;
int GPIO::pinDirections[] = {-1};
int GPIO::pwmValues[] = {-1};

void GPIO::setup()
{
    // /dev/mem file descriptor
    int mem;

    // Access physical memory
    if ((mem = open("/dev/mem", O_RDWR | O_SYNC)) < 0) {
        perror("Unable to access /dev/mem");
        exit(-1);
    }

    // Map memory
    gpioMap = mmap(
        NULL,                   // Any adddress in our space will do
        BLOCK_SIZE,             // Map length
        PROT_READ | PROT_WRITE, // Enable reading & writing to mapped memory
        MAP_SHARED,             // Shared with other processes
        mem,                    // File to map
        GPIO_BASE               // Offset to GPIO peripheral
    );

    close(mem); // No need to keep mem_fd open after mmap

    if (gpioMap == MAP_FAILED) {
        perror("Unable to map GPIO address space");
        exit(-1);
    }

    // Always use volatile pointer!
    gpio = (volatile unsigned int *) gpioMap;

    isSetup = true;
}

void GPIO::assertSetup()
{
    if (!isSetup) {
        fprintf(stderr, "GPIO setup has not been called\n");
        exit(-1);
    }
}

void GPIO::assertValidPin(int pin)
{
    for (size_t i = 0; i < sizeof(GPIO_PINS) / sizeof(int); i++) {
        if (pin == GPIO_PINS[i]) {
            return;
        }
    }

    fprintf(stderr, "Pin %d is not a valid GPIO pin\n", pin);
    exit(-1);
}

void GPIO::pinMode(int pin, direction d)
{
    assertSetup();
    assertValidPin(pin);

    switch (d) {
        case IN:
            INP_GPIO(pin);
        break;
        case OUT: {
            pinMode(pin, IN); // Set as input first
            OUT_GPIO(pin);
        } break;
        case PWM: {
            pinMode(pin, OUT);

            pthread_t thread;
            int *pinPtr = (int *) malloc(sizeof(int));
            *pinPtr = pin;
            pthread_create(&thread, NULL, GPIO::pwmThread, (void *) pinPtr);

            while (pwmValues[pin] == -1) {
                usleep(10);
            }
        } break;
    }

    pinDirections[pin] = d;
}

void GPIO::write(int pin, value v)
{
    assertSetup();
    assertValidPin(pin);

    if (v == HIGH) {
        GPIO_SET = 1 << pin;
    } else if (v == LOW) {
        GPIO_CLR = 1 << pin;
    }
}

GPIO::value GPIO::read(int pin)
{
    assertSetup();
    assertValidPin(pin);

    return GET_GPIO(pin) ? HIGH : LOW;
}

void GPIO::delay(unsigned int ms)
{
    struct timespec sleeper;
    unsigned int uSecs = ms % 1000000;
    unsigned int wSecs = ms / 1000000;

    if (ms == 0) {
        return;
    } else {
        sleeper.tv_sec  = wSecs;
        sleeper.tv_nsec = (long) (uSecs * 1000L);
        nanosleep(&sleeper, NULL);
    }
}

void *GPIO::pwmThread(void *data)
{
    int pin = *((int *) data), mark, space;

    if (pwmValues[pin] == -1) {
        pwmValues[pin] = 0;
    }

    while (true) {
        mark  = pwmValues[pin];
        space = 10 - mark;

        if (mark != 0) {
            write(pin, HIGH);
        }

        delay(mark * PULSE_TIME);

        if (space != 0) {
            write(pin, LOW);
        }

        delay(space * PULSE_TIME);
    }

    return NULL;
}

void GPIO::pwmWrite(int pin, int value)
{
    assertSetup();
    assertValidPin(pin);

    if (value < 0) {
        value = 0;
    } else if (value > PWM_MAX) {
        value = PWM_MAX;
    }

    pwmValues[pin] = value;
}

int GPIO::pwmRead(int pin)
{
    assertSetup();
    assertValidPin(pin);

    return pwmValues[pin];
}