#include "launcher_controller.hpp"

LauncherController::LauncherController()
{
    launcher.init();
}

void LauncherController::handle(char *data)
{
    int cmd;
    printf("received %d bytes");
    memcpy(&cmd, data, sizeof(int));

    Lib::println("CMD: %d", cmd);

    launcher.send(cmd);
}

int LauncherController::getDataSize()
{
    return sizeof(int);
}