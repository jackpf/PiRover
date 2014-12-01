#include "gtest/gtest.h"
#include "rover.hpp"

TEST(PWM, DigitalOn)
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

TEST(PWM, PWMRange)
{
    GPIO::pinMode(PIN_LEFT_A, GPIO::PWM);
    GPIO::pinMode(PIN_LEFT_B, GPIO::PWM);
    GPIO::pinMode(PIN_RIGHT_A, GPIO::PWM);
    GPIO::pinMode(PIN_RIGHT_B, GPIO::PWM);

    for (double i = PWM_MAX; i >= 0.0; i -= 0.1) {
        double mark, space;
        mark = i;
        space = PWM_MAX - mark;
        printf("[     INFO ] Setting pwm to %f (mark: %f, space: %f)\n", i, round(mark * PWM_FREQUENCY), round(space * PWM_FREQUENCY));

        GPIO::pwmWrite(PIN_LEFT_A, i);
        GPIO::pwmWrite(PIN_LEFT_B, 0);
        GPIO::pwmWrite(PIN_RIGHT_A, i);
        GPIO::pwmWrite(PIN_RIGHT_B, 0);

        sleep(1);
    }

    GPIO::pwmWrite(PIN_LEFT_A, 0);
    GPIO::pwmWrite(PIN_LEFT_B, 0);
    GPIO::pwmWrite(PIN_RIGHT_A, 0);
    GPIO::pwmWrite(PIN_RIGHT_B, 0);

    sleep(1);
}
