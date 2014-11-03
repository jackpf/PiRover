/**
 * Provides GPIO functionality
 * Code adapted from here: http://elinux.org/RPi_Low-level_peripherals
 * Accesses the physical GPIO memory space directly, therefore must be run as root!
 */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>

/**
 * IO access
 */
static volatile unsigned int *gpio;
static void *gpioMap;

/**
 * BCM2708 base address
 */
#define BCM2708_PERI_BASE   0x20000000

/**
 * GPIO controller base address
 */
#define GPIO_BASE           (BCM2708_PERI_BASE + 0x200000)

/**
 * Pi memory page size
 */
#define PAGE_SIZE           (4 * 1024)

/**
 * Pi memory block size
 */
#define BLOCK_SIZE          (4 * 1024)

/**
 * GPIO setup macros
 * Always use INP_GPIO(x) before using OUT_GPIO(x) or SET_GPIO_ALT(x,y)
 */
#define INP_GPIO(g)         *(gpio + ((g) / 10)) &= ~(7 << (((g) % 10) * 3))
#define OUT_GPIO(g)         *(gpio + ((g) / 10)) |=  (1 << (((g) % 10) * 3))
#define SET_GPIO_ALT(g, a)  *(gpio + ((g) / 10)) |=  (((a) <= 3 ? (a) + 4 : (a) == 4 ? 3 : 2) << (((g) % 10) * 3))

/**
 * Sets bits which are 1 ignores bits which are 0
 */
#define GPIO_SET            *(gpio + 7)

/**
 * Clears bits which are 1 ignores bits which are 0
 */
#define GPIO_CLR            *(gpio + 10)

/**
 * Returns 0 if LOW, (1<<g) if HIGH
 */
#define GET_GPIO(g)         (*(gpio + 13) & (1 << g))

/**
 * Pull up/pull down
 */
#define GPIO_PULL           *(gpio + 37)

/**
 * Pull up/pull down clock
 */
#define GPIO_PULLCLK0       *(gpio + 38)

/**
 * Number of GPIO pins
 */
#define NUM_PINS            26

/**
 * Valid GPIO pins
 */
const int GPIO_PINS[]       = {14, 15, 18, 23, 24, 25, 8,  7,
                               2,  3,  4,  17, 27, 22, 10, 9, 11};

#define PWM_MAX             10

#define PULSE_TIME          10

class GPIO
{
public:

    /**
     * Pin mode direction
     */
    enum direction {IN, OUT, PWM};

    /**
     * Pin value
     */
    enum value {HIGH, LOW};

private:

    /**
     * Set to true once setup has been called
     */
    static bool isSetup;

    /**
     * Direction of pins
     */
    static int pinDirections[NUM_PINS];

    /**
     * Pulse width modulation values
     */
    static int pwmValues[NUM_PINS];

    /**
     * Assert setup as been called
     */
    static void assertSetup();

    /**
     * Assert pin is a valid gpio pin
     *
     * @param pin   Pin number
     */
    static void assertValidPin(int pin);

public:

    /**
     * Set up GPIO access
     */
    static void setup();

    /**
     * Set the direction of a pin
     *
     * @param pin   Pin number
     * @param d     Direction of pin
     */
    static void pinMode(int pin, direction d);

    /**
     * Write to a pin
     *
     * @param pin   Pin number
     * @param v     Value of pin
     */
    static void write(int pin, value v);

    /**
     * Read value of a pin
     *
     * @param pin   Pin number
     * @return      Value of pin
     */
    static value read(int pin);

    /**
     * Sleep function
     *
     * @param ms    Milliseconds to sleep for
     */
    static void delay(unsigned int ms);

    /**
     * Internal pwm thread handler
     *
     * @param data  Pin number to run pwn on
     */
    static void *pwmThread(void *data);

    /**
     * Write a pwm value
     *
     * @param pin   Pin number to write to
     * @param value Pwm frequency (ratio out of PWM_MAX)
     */
    static void pwmWrite(int pin, int value);

    /**
     * Read pwm value of a pin
     *
     * @param pin   Pin to read
     * @return      Pwm frequency of pin
     */
    static int pwmRead(int pin);
};