#include "gpio.hpp"

volatile unsigned int *GPIO::gpio                       = NULL;
bool GPIO::isSetup                                      = false;
GPIO::Pin GPIO::pins[NUM_PINS]                          = {{}};
pthread_mutex_t GPIO::pwmThreadCancelMutex[NUM_PINS]    = {PTHREAD_MUTEX_INITIALIZER};
volatile bool GPIO::pwmThreadCancelRequest[NUM_PINS]    = {false};

void GPIO::setup()
{
    if (isSetup) {
        return;
    }

    // /dev/mem file descriptor
    int mem;

    // Access physical memory
    if ((mem = open("/dev/mem", O_RDWR | O_SYNC)) < 0) {
        perror("Unable to access /dev/mem");
        exit(-1);
    }

    // Map memory
    void *gpioMap = mmap(
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

    // Get pin states
    for (size_t i = 0; i < sizeof(GPIO_PINS) / sizeof(int); i++) {
        pins[GPIO_PINS[i]].mode = pinMode(GPIO_PINS[i]);
        pins[GPIO_PINS[i]].value = read(GPIO_PINS[i]);
    }
}

void GPIO::assertSetup()
{
    if (!isSetup) {
        Lib::println(stderr, "GPIO setup has not been called");
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

    Lib::println(stderr, "Pin %d is not a valid GPIO pin", pin);
    exit(-1);
}

void GPIO::pinMode(int pin, direction d)
{
    assertSetup();
    assertValidPin(pin);

    // If switching mode from PWM make sure the PWM thread is cancelled
    if (pins[pin].mode == PWM) {
        pthread_mutex_lock(&pwmThreadCancelMutex[pin]);
        pwmThreadCancelRequest[pin] = true;
        pthread_mutex_unlock(&pwmThreadCancelMutex[pin]);
        pthread_join(pins[pin].pwmThread, NULL);
    }

    pins[pin].mode = d;

    /**
     * The first 6 32 bit registers starting from the GPIO base address are the function select registers
     * Register 0: pins 0 - 9
     * Register 1: pins 10 - 19
     * Register 2: pins 20 - 29
     * Register 3: pins 30 - 39
     * Register 4: pins 40 - 49
     * Register 5: pins 50 - 53
     *
     * Each pin then has 3 bits to set it as an input, output, or one of 6 alternate functions (8 possibilities = 3 bits)
     * 000 = input
     * 001 = output
     * 100 = alternate function 0
     * 101 = alternate function 1
     * etc...
     *
     * With pin 1 starting at bits 0-2, pin 2 at 3-5, etc...
     */

    switch (d) {
        case IN:
            /**
             * (pin % 10) * 3 will calculate the position in the register for the pin
             * since the register is split into 10 pins (pin % 10) and each pin has 3 bits (*3)
             * 7 = 111 which is then shifted to the correct position in the register
             * and everything is inverted and anded to set the correct bits to 000 (000 = input, see above)
             */
            *(gpio + REGISTER_FNSELECT + (pin / 10)) &= ~(7 << ((pin % 10) * 3));
            break;
        case OUT: {
            /**
             * Pin must be set as an input before it is set as an output
             */
            *(gpio + REGISTER_FNSELECT + (pin / 10)) &= ~(7 << ((pin % 10) * 3));

            /**
             * Does the same as setting as input, but sets the first bit to 1 = output
             */
            *(gpio + REGISTER_FNSELECT + (pin / 10)) |= 1 << ((pin % 10) * 3);
        } break;
        case PWM: {
            // PWM must be set as output
            *(gpio + REGISTER_FNSELECT + (pin / 10)) |= 1 << ((pin % 10) * 3);

            // Create a pointer to the pin number to pass to the thread
            int *pinPtr = (int *) malloc(sizeof(int));
            *pinPtr = pin;

            pins[pin].pwmValue = -1;

            // Spawn a thread to handle PWM
            pthread_create(&pins[pin].pwmThread, NULL, GPIO::_pwmThread, (void *) pinPtr);

            // Only return when the pin has been set up by the new thread
            while (pins[pin].pwmValue == -1) {
                usleep(10);
            }
        } break;
    }
}

GPIO::direction GPIO::pinMode(int pin)
{
    // @TODO: This only checks the if the first bit is set, so won't actually be accurate if alt functions are used
    // However this doesn't really matter since alt functions are not currently implemented anyway
    return *(gpio + REGISTER_FNSELECT + (pin / 10)) & (1 << ((pin % 10) * 3)) ? OUT : IN;
}

void GPIO::write(int pin, value v)
{
    assertSetup();
    assertValidPin(pin);

    if (pins[pin].mode != OUT && pins[pin].mode != PWM) {
        Lib::println(stderr, "Warning: pin %d is not set as output", pin);
    }

    /**
     * The set and clear registers
     */
    if (v == HIGH) {
        *(gpio + REGISTER_SET + (pin / 32)) = 1 << (pin % 32);
    } else if (v == LOW) {
        *(gpio + REGISTER_CLEAR + (pin / 32)) = 1 << (pin % 32);
    }
}

GPIO::value GPIO::read(int pin)
{
    assertSetup();
    assertValidPin(pin);

    return *(gpio + REGISTER_GET + (pin / 32)) & (1 << (pin % 32)) ? HIGH : LOW;
}

void GPIO::delay(unsigned int ms)
{
    if (ms == 0) {
        return;
    }

    struct timespec sleeper;
    unsigned int uSecs = ms % 1000000;
    unsigned int wSecs = ms / 1000000;

    sleeper.tv_sec  = wSecs;
    sleeper.tv_nsec = (long) (uSecs * 1000L);
    nanosleep(&sleeper, NULL);
}

void *GPIO::_pwmThread(void *data)
{
    int pin = *((int *) data);
    double mark, space;

    if (pins[pin].pwmValue == -1) {
        pins[pin].pwmValue = 0.0;
    }

    while (true) {
        mark  = pins[pin].pwmValue;
        space = PWM_MAX - mark;

        if (mark != 0) {
            write(pin, HIGH);
            delay(round(mark * PWM_FREQUENCY));
        }

        if (space != 0) {
            write(pin, LOW);
            delay(round(space * PWM_FREQUENCY));
        }

        pthread_mutex_lock(&pwmThreadCancelMutex[pin]);
        if (pwmThreadCancelRequest[pin]) {
            pwmThreadCancelRequest[pin] = false;
            pthread_mutex_unlock(&pwmThreadCancelMutex[pin]);
            break;
        } else {
            pthread_mutex_unlock(&pwmThreadCancelMutex[pin]);
        }
    }

    free((int *) data);

    return NULL;
}

void GPIO::pwmWrite(int pin, double value)
{
    assertSetup();
    assertValidPin(pin);

    if (pins[pin].mode != PWM) {
        Lib::println(stderr, "Warning: pin %d is not set as PWM output", pin);
    }

    if (value < 0.0) {
        Lib::println(stderr, "Warning: incorrect pwm value of %f", value);
        value = 0.0;
    } else if (value > PWM_MAX) {
        value = PWM_MAX;
    }

    pins[pin].pwmValue = value;
}

int GPIO::pwmRead(int pin)
{
    assertSetup();
    assertValidPin(pin);

    return pins[pin].pwmValue;
}