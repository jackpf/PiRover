#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <wiringPi.h>

#include "lib/lib.hpp"

#define PIN_LEFT_A     9
#define PIN_LEFT_B     7
#define PIN_RIGHT_A 2
#define PIN_RIGHT_B 3

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
    void process(int, char **);
    void forward();
    void backward();
    void left();
    void right();
    void stop();
};