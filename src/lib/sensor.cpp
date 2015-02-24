#include "sensor.hpp"

Sensor::Sensor()
{
    GPIO::setup();

    GPIO::pinMode(PIN_TRIGGER, GPIO::OUT);
    GPIO::pinMode(PIN_ECHO, GPIO::IN);
    GPIO::write(PIN_TRIGGER, GPIO::LOW);
}

double Sensor::calculateDistance()
{
    // Send trigger pulse
    GPIO::write(PIN_TRIGGER, GPIO::HIGH);
    GPIO::delay(10);
    GPIO::write(PIN_TRIGGER, GPIO::LOW);

    timespec start, stop;

    while (GPIO::read(PIN_ECHO) == GPIO::LOW);

    clock_gettime(CLOCK_REALTIME, &start);

    while (GPIO::read(PIN_ECHO) == GPIO::HIGH);

    clock_gettime(CLOCK_REALTIME, &stop);

    /*  Convert to microseconds
        Multiply by speed of sound
        Divide by 2 to get one way distance
     */

    return (double) (stop.tv_nsec - start.tv_nsec) / 1000.0 * 0.034 / 2;
}