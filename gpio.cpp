#include "gpio.hpp"

bool GPIO::isSetup = false;

void GPIO::setup()
{
    // /dev/mem file descriptor
    int mem;

    // Access physical memory
    if ((mem = open("/dev/mem", O_RDWR | O_SYNC)) < 0) {
        perror("Unable to access /dev/mem: %s\n");
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
        perror("Unable to map GPIO address space: %s\n");
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

void GPIO::pinMode(int pin, direction d)
{
    assertSetup();

    if (d == IN) {
        INP_GPIO(pin);
    } else if (d == OUT) {
        INP_GPIO(pin); // Set as input first
        OUT_GPIO(pin);
    }
}

void GPIO::write(int pin, value v)
{
    assertSetup();

    if (v == HIGH) {
        GPIO_SET = 1 << pin;
    } else if (v == LOW) {
        GPIO_CLR = 1 << pin;
    }
}

GPIO::value GPIO::read(int pin)
{
    assertSetup();

    return GET_GPIO(pin) ? HIGH : LOW;
}

