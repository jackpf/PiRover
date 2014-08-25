#include "rover.hpp"

Rover::Rover()
{
    args = new RoverArgs;
}

void Rover::setup()
{
    wiringPiSetup();

    pinMode(PIN_LEFT_A, OUTPUT);
    pinMode(PIN_LEFT_B, OUTPUT);
    pinMode(PIN_RIGHT_A, OUTPUT);
    pinMode(PIN_RIGHT_B, OUTPUT);
}

/*void Rover::process(int argc, char **argv)
{
    assert_args(argc, 1);
    char *cmd = argv[1];

    if (strcmp(cmd, "forward") == 0) {
        assert_args(argc, 2);
        args->time = atof(argv[2]);
        forward();
    } else if (strcmp(cmd, "backward") == 0) {
        assert_args(argc, 2);
        args->time = atof(argv[2]);
        backward();
    } else if (strcmp(cmd, "left") == 0) {
        assert_args(argc, 2);
        args->time = atof(argv[2]);
        left();
    } else if (strcmp(cmd, "right") == 0) {
        assert_args(argc, 2);
        args->time = atof(argv[2]);
        right();
    } else if (strcmp(cmd, "stop") == 0) {
        stop();
    }
}*/

void Rover::process(int keyCode)
{
    switch (keyCode) {
        case 65:
            forward();
        break;
        case 66:
            backward();
        break;
        case 68:
            left();
        break;
        case 67:
            right();
        break;
        default:
            stop();
        break;
    }
}

void Rover::forward()
{
    digitalWrite(PIN_LEFT_A, HIGH);
    digitalWrite(PIN_LEFT_B, LOW);
    digitalWrite(PIN_RIGHT_A, HIGH);
    digitalWrite(PIN_RIGHT_B, LOW);
}

void Rover::backward()
{
    digitalWrite(PIN_LEFT_A, LOW);
    digitalWrite(PIN_LEFT_B, HIGH);
    digitalWrite(PIN_RIGHT_A, LOW);
    digitalWrite(PIN_RIGHT_B, HIGH);
}

void Rover::left()
{
    digitalWrite(PIN_LEFT_A, LOW);
    digitalWrite(PIN_LEFT_B, HIGH);
    digitalWrite(PIN_RIGHT_A, HIGH);
    digitalWrite(PIN_RIGHT_B, LOW);
}

void Rover::right()
{
    digitalWrite(PIN_LEFT_A, HIGH);
    digitalWrite(PIN_LEFT_B, LOW);
    digitalWrite(PIN_RIGHT_A, LOW);
    digitalWrite(PIN_RIGHT_B, HIGH);
}

void Rover::stop()
{
    digitalWrite(PIN_LEFT_A, LOW);
    digitalWrite(PIN_LEFT_B, LOW);
    digitalWrite(PIN_RIGHT_A, LOW);
    digitalWrite(PIN_RIGHT_B, LOW);
}
