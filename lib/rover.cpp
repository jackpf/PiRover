#include "rover.hpp"

void Rover::setup()
{
    wiringPiSetup();

    softPwmCreate(PIN_LEFT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_LEFT_B, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_B, PWM_OFF, PWM_MAX);
}

Rover::MotorValues Rover::resetMotorValues()
{
    motors = {{0, 0}, {0, 0}};

    return motors;
}

Rover::MotorValues Rover::calculateMotorValues(int acceleration, int steering)
{
    resetMotorValues();

    int left = acceleration, right = acceleration;

    if (steering < 0) {
        steering = -steering;
        left -= round((float) acceleration * (2.0 * (float) steering / 10.0));
    } else if (steering > 0) {
        right -= round((float) acceleration * (2.0 * (float) steering / 10.0));
    }

    if (left > 0) {
        motors.left.motorA = left;
        motors.left.motorB = 0;
    } else if (left < 0) {
        motors.left.motorA = 0;
        motors.left.motorB = -left;
    }

    if (right > 0) {
        motors.right.motorA = right;
        motors.right.motorB = 0;
    } else if (right < 0) {
        motors.right.motorA = 0;
        motors.right.motorB = -right;
    }

    return motors;
}

void Rover::write()
{
    softPwmWrite(PIN_LEFT_A, motors.left.motorA);
    softPwmWrite(PIN_LEFT_B, motors.left.motorB);
    softPwmWrite(PIN_RIGHT_A, motors.right.motorA);
    softPwmWrite(PIN_RIGHT_B, motors.left.motorB);
}

Rover::MotorValues Rover::getMotorValues()
{
    return motors;
}
