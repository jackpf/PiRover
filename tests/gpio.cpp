#include "gtest/gtest.h"
#include "gpio.hpp"

/**
 * Not the greatest tests since static methods will affect concurrent tests
 * Therefore this test must be run first
 */
TEST(GPIO, WriteWithoutSetup)
{
    const int pin = 11;

    EXPECT_DEATH(GPIO::pinMode(pin, GPIO::OUT), "GPIO setup has not been called");
}

TEST(GPIO, PinModeWriteLowAndRead)
{
    const int pin = 11;

    GPIO::setup();
    GPIO::pinMode(pin, GPIO::OUT);
    GPIO::write(pin, GPIO::LOW);
    EXPECT_EQ(GPIO::read(pin), GPIO::LOW);
}

TEST(GPIO, PinModeWriteHighAndRead)
{
    const int pin = 11;

    GPIO::setup();
    GPIO::pinMode(pin, GPIO::OUT);
    GPIO::write(pin, GPIO::HIGH);
    EXPECT_EQ(GPIO::read(pin), GPIO::HIGH);
}

TEST(GPIO, WriteWithoutSetup)
{
    GPIO::setup();

    EXPECT_DEATH(GPIO::pinMode(394, GPIO::OUT), "Pin 394 is not a valid GPIO pin");
}