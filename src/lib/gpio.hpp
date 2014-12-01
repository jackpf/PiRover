/**
 * Provides GPIO functionality
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
#include <math.h>
#include "lib.hpp"

/**
 * BCM2708 base address
 */
#define BCM2835_BASE        0x20000000

/**
 * GPIO controller base address
 */
#define GPIO_BASE           (BCM2835_BASE + 0x200000)

/**
 * Pi memory page size
 */
#define PAGE_SIZE           (4 * 1024)

/**
 * Pi memory block size
 */
#define BLOCK_SIZE          (4 * 1024)

/**
 * GPIO register memory address offsets
 */
#define REGISTER_FNSELECT   0
#define REGISTER_SET        7
#define REGISTER_CLEAR      10
#define REGISTER_GET        13

/**
 * Number of GPIO pins
 */
#define NUM_PINS            26

/**
 * Valid GPIO pins
 */
const int GPIO_PINS[]       = {14, 15, 18, 23, 24, 25, 8,  7,
                               2,  3,  4,  17, 27, 22, 10, 9, 11};

/**
 * Max PWM value
 */
#define PWM_MAX             1.0

/**
 * PWM pulse time
 */
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

    struct Pin {
        enum direction mode;
        enum value value;
        pthread_t pwmThread;
        double pwmValue;
    };

private:

    /**
     * IO access
     */
    static volatile unsigned int *gpio;

    /**
     * Set to true once setup has been called
     */
    static bool isSetup;

    /**
     * Pin states
     */
    static struct Pin pins[NUM_PINS];

    /**
     * Mutex lock for pwm thread cancel requests
     */
    static pthread_mutex_t pwmThreadCancelMutex;

    /**
     * Array of cancel requests
     * pwmThreadCancelRequest[pinNumber] = true will cause thread to be cancelled after its current loop
     */
    static volatile bool pwmThreadCancelRequest[NUM_PINS];

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
     * Read the direction of a pin
     *
     * @param pin   Pin number
     * @return      Direction of pin
     */
    static direction pinMode(int pin);

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
    static void *_pwmThread(void *data);

    /**
     * Write a pwm value
     *
     * @param pin   Pin number to write to
     * @param value Pwm duty cycle value
     */
    static void pwmWrite(int pin, double value);

    /**
     * Read pwm value of a pin
     *
     * @param pin   Pin to read
     * @return      Pwm frequency of pin
     */
    static int pwmRead(int pin);
};