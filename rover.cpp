#include "rover.hpp"

Rover::Rover()
{
    args = new RoverArgs;
}

void Rover::setup()
{
    wiringPiSetup();

    softPwmCreate(PIN_LEFT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_LEFT_B, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_A, PWM_OFF, PWM_MAX);
    softPwmCreate(PIN_RIGHT_B, PWM_OFF, PWM_MAX);
}

void Rover::process(int keyCode)
{
    static int v, lastKey = -1;

    if (lastKey == keyCode) {
        v += 25;
    } else {
        v = 25;
    }

    lastKey = keyCode;

    switch (keyCode) {
        case 65:
            forward(v);
        break;
        case 66:
            backward(v);
        break;
        case 68:
            left(v);
        break;
        case 67:
            right(v);
        break;
        default:
            stop();
        break;
    }
}

void Rover::forward(int v)
{
    softPwmWrite(PIN_LEFT_A, v);
    softPwmWrite(PIN_LEFT_B, PWM_OFF);
    softPwmWrite(PIN_RIGHT_A, v);
    softPwmWrite(PIN_RIGHT_B, PWM_OFF);
}

void Rover::backward(int v)
{
    softPwmWrite(PIN_LEFT_A, PWM_OFF);
    softPwmWrite(PIN_LEFT_B, v);
    softPwmWrite(PIN_RIGHT_A, PWM_OFF);
    softPwmWrite(PIN_RIGHT_B, v);
}

void Rover::left(int v)
{
    softPwmWrite(PIN_LEFT_A, PWM_OFF);
    softPwmWrite(PIN_LEFT_B, v);
    softPwmWrite(PIN_RIGHT_A, v);
    softPwmWrite(PIN_RIGHT_B, PWM_OFF);
}

void Rover::right(int v)
{
    softPwmWrite(PIN_LEFT_A, v);
    softPwmWrite(PIN_LEFT_B, PWM_OFF);
    softPwmWrite(PIN_RIGHT_A, v);
    softPwmWrite(PIN_RIGHT_B, v);
}

void Rover::stop()
{
    softPwmWrite(PIN_LEFT_A, PWM_OFF);
    softPwmWrite(PIN_LEFT_B, PWM_OFF);
    softPwmWrite(PIN_RIGHT_A, PWM_OFF);
    softPwmWrite(PIN_RIGHT_B, PWM_OFF);
}
