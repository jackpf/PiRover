#include "rover.hpp"

void Rover::setup()
{
    wiringPiSetup();

    softPwmCreate(PIN_LEFT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_LEFT_B, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_B, PWM_OFF, PWM_MAX);
}

MotorValues Rover::calculate(int acceleration, int steering)
{
    MotorValues values = {0, 0, 0, 0};

    int left = acceleration, right = acceleration;

    if (steering < 0) {
        steering = -steering;
        left -= round((float) acceleration * (2.0 * (float) steering / 10.0));
    } else if (steering > 0) {
        right -= round((float) acceleration * (2.0 * (float) steering / 10.0));
    }

    if (left > 0) {
        values.leftA = left;
        values.leftB = 0;
    } else if (left < 0) {
        values.leftA = 0;
        values.leftB = -left;
    }

    if (right > 0) {
        values.rightA = right;
        values.rightB = 0;
    } else if (right < 0) {
        values.rightA = 0;
        values.rightB = -right;
    }

    return values;
}

void Rover::accelerate(MotorValues values)
{
    softPwmWrite(PIN_LEFT_A, values.leftA);
    softPwmWrite(PIN_LEFT_B, values.leftB);
    softPwmWrite(PIN_RIGHT_A, values.rightA);
    softPwmWrite(PIN_RIGHT_B, values.rightB);
}

MotorValues Rover::process(int acceleration, int steering)
{
    accelerate(
        calculate(acceleration, steering)
    );
}
