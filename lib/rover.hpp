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

private:

    MotorValues motors;

public:

    /**
     * Initialise rover
     */
    void setup();

    /**
     * Reset motor values back to 0
     *
     * @return              Motor values
     */
    MotorValues resetMotorValues();

    /**
     * Calculate motor values from given acceleration and steering,
     * and send values to motors
     *
     * @param acceleration  Acceleration of rover on a scale of 1-10
     * @param steering      Steering position of rover on a scale of -10-10
     * @return              Motor values
     */
    MotorValues calculateMotorValues(int acceleration, int steering);

    /**
     * Write motor values to GPIO pins
     */
    void write();

    /**
     * Get motor values
     *
     * @return              Motor values struct
     */
    MotorValues getMotorValues();
};