#include "rover_controller.hpp"

RoverController::RoverController()
{
    rover.init();
}

void RoverController::handle(char *data)
{
    int accelerationPosition, steeringPosition;

    memcpy(&accelerationPosition, data, sizeof(int));
    memcpy(&steeringPosition, data + sizeof(int), sizeof(int));

    for (int i = 0; i < getDataSize();i++) {
        Lib::println("%d) %02X",i,(int)data[i]);
    }

    Lib::println("A: %d, S: %d", accelerationPosition, steeringPosition);

    /*if (server.receive(conn, (void *) &accelerationPosition, sizeof(int)) <= 0) {
        break;
    }
    if (server.receive(conn, (void *) &steeringPosition, sizeof(int)) <= 0) {
        break;
    }

    rover.calculateMotorValues(accelerationPosition, steeringPosition);
    rover.write();*/
}

int RoverController::getDataSize()
{
    return sizeof(int) * 2;
}