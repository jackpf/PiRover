#include "shutdown_controller.hpp"

Handler::ReturnData ShutdownController::handle(char *data)
{
    system("halt");

    return NO_DATA;
}

void ShutdownController::cleanup()
{

}

int ShutdownController::getDataSize()
{
    return 0;
}