#include "servo_controller.hpp"

ServoController::ServoController()
{

}

Handler::ReturnData ServoController::handle(char *data)
{
    /* This is where the servo would be controlled (if I could get it to work properly) */

    return NO_DATA;
}

void ServoController::cleanup()
{

}

int ServoController::getDataSize()
{
    return sizeof(int);
}