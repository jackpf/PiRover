#include "launcher_controller.hpp"

LauncherController::LauncherController()
{
    launcher.init();
}

Handler::ReturnData LauncherController::handle(char *data)
{
    int cmd;

    memcpy(&cmd, data, sizeof(int));

    launcher.send(cmd);

    return NO_DATA;
}

void LauncherController::cleanup()
{

}

int LauncherController::getDataSize()
{
    return sizeof(int);
}