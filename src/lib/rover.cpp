#include "rover.hpp"

void Rover::init()
{
    GPIO::setup();

    GPIO::pinMode(PIN_LEFT_A, GPIO::PWM);
    GPIO::pinMode(PIN_LEFT_B, GPIO::PWM);
    GPIO::pinMode(PIN_RIGHT_A, GPIO::PWM);
    GPIO::pinMode(PIN_RIGHT_B, GPIO::PWM);
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
        motors.left.motorA = left / 10.0;
        motors.left.motorB = 0.0;
    } else if (left < 0) {
        motors.left.motorA = 0.0;
        motors.left.motorB = -left / 10.0;
    }

    if (right > 0) {
        motors.right.motorA = right / 10.0;
        motors.right.motorB = 0.0;
    } else if (right < 0) {
        motors.right.motorA = 0.0;
        motors.right.motorB = -right / 10.0;
    }

    return motors;
}

void Rover::write()
{
    GPIO::pwmWrite(PIN_LEFT_A, motors.left.motorA);
    GPIO::pwmWrite(PIN_LEFT_B, motors.left.motorB);
    GPIO::pwmWrite(PIN_RIGHT_A, motors.right.motorA);
    GPIO::pwmWrite(PIN_RIGHT_B, motors.right.motorB);
}

Rover::MotorValues Rover::getMotorValues()
{
    return motors;
}