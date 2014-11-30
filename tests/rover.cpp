#include "gtest/gtest.h"
#include "rover.hpp"

TEST(Rover, MoveForward)
{
    GPIO::setup();

    GPIO::pinMode(PIN_LEFT_A, GPIO::OUT);
    GPIO::pinMode(PIN_LEFT_B, GPIO::OUT);
    GPIO::pinMode(PIN_RIGHT_A, GPIO::OUT);
    GPIO::pinMode(PIN_RIGHT_B, GPIO::OUT);

    GPIO::write(PIN_LEFT_A, GPIO::HIGH);
    GPIO::write(PIN_LEFT_B, GPIO::LOW);
    GPIO::write(PIN_RIGHT_A, GPIO::HIGH);
    GPIO::write(PIN_RIGHT_B, GPIO::LOW);

    sleep(1);

    GPIO::write(PIN_LEFT_A, GPIO::LOW);
    GPIO::write(PIN_LEFT_B, GPIO::LOW);
    GPIO::write(PIN_RIGHT_A, GPIO::LOW);
    GPIO::write(PIN_RIGHT_B, GPIO::LOW);

    sleep(1);
}

TEST(Rover, PWM)
{
    GPIO::pinMode(PIN_LEFT_A, GPIO::PWM);
    GPIO::pinMode(PIN_LEFT_B, GPIO::PWM);
    GPIO::pinMode(PIN_RIGHT_A, GPIO::PWM);
    GPIO::pinMode(PIN_RIGHT_B, GPIO::PWM);

    GPIO::pwmWrite(PIN_LEFT_A, 5);
    GPIO::pwmWrite(PIN_LEFT_B, 0);
    GPIO::pwmWrite(PIN_RIGHT_A, 5);
    GPIO::pwmWrite(PIN_RIGHT_B, 0);

    sleep(1);

    GPIO::pwmWrite(PIN_LEFT_A, 0);
    GPIO::pwmWrite(PIN_LEFT_B, 0);
    GPIO::pwmWrite(PIN_RIGHT_A, 0);
    GPIO::pwmWrite(PIN_RIGHT_B, 0);

    sleep(1);
}

TEST(Rover, Stop)
{
    GPIO::write(PIN_LEFT_A, GPIO::LOW);
    GPIO::write(PIN_LEFT_B, GPIO::LOW);
    GPIO::write(PIN_RIGHT_A, GPIO::LOW);
    GPIO::write(PIN_RIGHT_B, GPIO::LOW);
}