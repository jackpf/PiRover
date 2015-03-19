#ifndef H_SENSOR
#define H_SENSOR

#include <time.h>
#include "gpio.hpp"

#define PIN_TRIGGER 23
#define PIN_ECHO    24

class Sensor
{
public:
    Sensor();
    double calculateDistance();
};

#endif