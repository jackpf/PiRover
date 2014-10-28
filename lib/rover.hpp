#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

#include <wiringPi.h>
#include <softPwm.h>

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
private:

    MotorValues motors;

public:

    /**
     * Motor values
     */
    typedef struct {
        int motorA;
        int motorB;
    } Motor;

    typedef struct {
        Motor left;
        Motor right;
    } MotorValues;

    /**
     * Initialise rover
     */
    void setup();

    /**
     * Calculate motor values from given acceleration and steering,
     * and send values to motors
     *
     * @param acceleration  Acceleration of rover on a scale of 1-10
     * @param steering      Steering position of rover on a scale of -10-10
     */
    MotorValues process(int acceleration, int steering);

    /**
     * Get motor values
     *
     * @return              Motor values struct
     */
    MotorValues getMotors();
};