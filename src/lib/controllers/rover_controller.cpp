#include "rover_controller.hpp"

RoverController::RoverController()
{
    rover.init();
}

Handler::ReturnData RoverController::handle(char *data)
{
    int accelerationPosition, steeringPosition;

    memcpy(&accelerationPosition, data, sizeof(int));
    memcpy(&steeringPosition, data + sizeof(int), sizeof(int));

    rover.calculateMotorValues(accelerationPosition, steeringPosition);
    rover.write();

    return NO_DATA;
}

void RoverController::cleanup()
{
    rover.resetMotorValues();
    rover.write();
}

int RoverController::getDataSize()
{
    return sizeof(int) * 2;
}