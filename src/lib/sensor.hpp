#ifndef H_SENSOR
#define H_SENSOR

#include <time.h>
#include "gpio.hpp"

#define PIN_TRIGGER 24
#define PIN_ECHO    25

class Sensor
{
public:
    Sensor();
    double calculateDistance();
};

#endif