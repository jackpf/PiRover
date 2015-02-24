#include "sensor_controller.hpp"
#include "sensor.hpp"

SensorController::SensorController()
{
    //retVal = (double *) malloc(sizeof(int));
}

SensorController::~SensorController()
{
    delete(retVal);
}

Handler::ReturnData SensorController::handle(char *data)
{
    ReturnData returnData;

    retVal = new int(round(sensor.calculateDistance()));

    returnData.len = sizeof(int);
    returnData.data = retVal;

    return returnData;
}

void SensorController::cleanup()
{

}

int SensorController::getDataSize()
{
    return 0;
}