#include "shutdown_controller.hpp"

void ShutdownController::handle(char *data)
{
    system("halt");
}

void ShutdownController::cleanup()
{

}

int ShutdownController::getDataSize()
{
    return 0;
}