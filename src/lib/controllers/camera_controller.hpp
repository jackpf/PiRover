#include "handler.hpp"
#include "lib.hpp"
#include <signal.h>

class CameraController : public Handler
{
private:
    bool motionDetection;
public:
    CameraController();
    ReturnData handle(char *data);
    void cleanup();
    int getDataSize();
};