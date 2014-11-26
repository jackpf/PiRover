#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <raspicam/raspicam_cv.h>

#define CAM_WIDTH   320
#define CAM_HEIGHT  240
#define CAM_RGB     true

using namespace cv;
using namespace raspicam;

class PiCam
{
private:
    RaspiCam_Cv camera;

public:
    bool setup();

    void close();

    vector<uchar> getFrame();
};