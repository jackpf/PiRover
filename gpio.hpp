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

// I/O access
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

/* GPIO setup macros
   Always use INP_GPIO(x) before using OUT_GPIO(x) or SET_GPIO_ALT(x,y) */
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

class GPIO
{
private:
    /**
     * Set to true once setup has been called
     */
    static bool isSetup;

    /**
     * Assert setup as been called
     */
    static void assertSetup();

public:
    enum direction {IN, OUT};
    enum value {HIGH, LOW};

    static void setup();
    static void pinMode(int pin, direction d);
    static void write(int pin, value v);
    static value read(int pin);
};