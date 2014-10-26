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

typedef struct {
    int leftA;
    int leftB;
    int rightA;
    int rightB;
} MotorValues;

class Rover
{
public:

    /**
     * Initialise rover
     */
    void setup();

    /**
     * Calculate motor values from given acceleration and steering
     *
     * @param acceleration  Acceleration of rover on a scale of 1-10
     * @param steering      Steering position of rover on a scale of -10-10
     * @return              Motor values
     */
    MotorValues calculate(int acceleration, int steering);

    /**
     * Accelerate motors
     *
     * @param values        Motor values to be applied
     */
    void accelerate(MotorValues values);

    /**
     * Calculate values and accelerate motors
     *
     * @param acceleration  Acceleration of rover on a scale of 1-10
     * @param steering      Steering position of rover on a scale of -10-10
     */
    void process(int acceleration, int steering);
};