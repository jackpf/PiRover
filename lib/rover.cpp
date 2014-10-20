#include "rover.hpp"

void Rover::setup()
{
    wiringPiSetup();

    softPwmCreate(PIN_LEFT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_LEFT_B, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_B, PWM_OFF, PWM_MAX);
}

void Rover::process(int acceleration, int steering)
{
    int left = acceleration, right = acceleration;

    if (steering < 0) {
        steering = -steering;
        left -= round((float) acceleration * (2.0 * (float) steering / 10.0));
    } else if (steering > 0) {
        right -= round((float) acceleration * (2.0 * (float) steering / 10.0));
    }

    int leftA = 0, leftB = 0, rightA = 0, rightB = 0;

    if (left > 0) {
        leftA = left;
        leftB = 0;
    } else if (left < 0) {
        leftA = 0;
        leftB = -left;
    }

    if (right > 0) {
        rightA = right;
        rightB = 0;
    } else if (right < 0) {
        rightA = 0;
        rightB = -right;
    }

    printf("left %d, right %d, la %d, lb %d, ra %d, rb %d\n", left, right, leftA, leftB, rightA, rightB);

    accelerate(leftA, leftB, rightA, rightB);
}

void Rover::accelerate(int leftA, int leftB, int rightA, int rightB)
{
    softPwmWrite(PIN_LEFT_A, leftA);
    softPwmWrite(PIN_LEFT_B, leftB);
    softPwmWrite(PIN_RIGHT_A, rightA);
    softPwmWrite(PIN_RIGHT_B, rightB);
}
