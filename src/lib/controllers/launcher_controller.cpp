#include "launcher_controller.hpp"

LauncherController::LauncherController()
{
    launcher.init();
}

void LauncherController::handle(void *data, int len)
{
    int cmd;
    printf("received %d bytes", len);
    memcpy(&cmd, data, sizeof(int));

    Lib::println("CMD: %d", cmd);

    launcher.send(cmd);
}

int LauncherController::getDataSize()
{
    return sizeof(int);
}