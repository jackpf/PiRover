#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include <wiringPi.h>
#include <softPwm.h>

#include "lib/lib.hpp"

/**
 * GPIO pin numbers
 */
#define PIN_LEFT_A  9
#define PIN_LEFT_B  7
#define PIN_RIGHT_A 2
#define PIN_RIGHT_B 3
#define PWM_OFF     0
#define PWM_MAX     10

class Rover
{
public:

    /**
     * Initialise rover
     */
    void setup();

    /**
     * Process given acceleration and steering to produce motor values
     *
     * @param acceleration  Acceleration of rover on a scale of 1-10
     * @param steering      Steering position of rover on a scale of -10-10
     */
    void process(int acceleration, int steering);

    /**
     * Accelerate motors
     *
     * @param leftA         Left forward
     * @param leftB         Left backward
     * @param rightA        Right forward
     * @param rightB        Right backward
     */
    void accelerate(int leftA, int leftB, int rightA, int rightB);
};