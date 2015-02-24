#include "handler.hpp"
#include "lib.hpp"
#include "sensor.hpp"

class SensorController : public Handler
{
private:
    Sensor sensor;
    int *retVal;
public:
    SensorController();
    ~SensorController();
    ReturnData handle(char *data);
    void cleanup();
    int getDataSize();
};