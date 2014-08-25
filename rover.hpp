#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <wiringPi.h>
#include <softPwm.h>

#include "lib/lib.hpp"

#define PIN_LEFT_A  9
#define PIN_LEFT_B  7
#define PIN_RIGHT_A 2
#define PIN_RIGHT_B 3
#define PWM_OFF     0
#define PWM_MAX     100

struct RoverArgs {
    float time;
    float velocity;
};

class Rover
{
private:
    RoverArgs *args;

public:
    Rover();
    void setup();
    void process(int);
    void forward(int);
    void backward(int);
    void left(int);
    void right(int);
    void stop();
};