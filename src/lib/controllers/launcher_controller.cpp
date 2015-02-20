#include "launcher_controller.hpp"

LauncherController::LauncherController()
{
    launcher.init();
}

void LauncherController::handle(char *data)
{
    int cmd;

    memcpy(&cmd, data, sizeof(int));

    launcher.send(cmd);
}

void LauncherController::cleanup()
{

}

int LauncherController::getDataSize()
{
    return sizeof(int);
}