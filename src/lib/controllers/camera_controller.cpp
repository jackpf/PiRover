#include "camera_controller.hpp"

CameraController::CameraController()
{
    motionDetection = false;
}

Handler::ReturnData CameraController::handle(char *data)
{
    motionDetection = !motionDetection;

    char line[32];
    FILE *cmd = popen("pidof camera", "r");

    fgets(line, 32, cmd);
    pid_t pid = strtoul(line, NULL, 10);

    kill(pid, SIGUSR1);

    pclose(cmd);

    return NO_DATA;
}

void CameraController::cleanup()
{
    // If motion detection is on, turn it off again
    if (motionDetection) {
        handle(NULL);
    }
}

int CameraController::getDataSize()
{
    return 0;
}