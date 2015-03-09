#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <raspicam/raspicam_cv.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#define CAM_WIDTH   320
#define CAM_HEIGHT  240
#define CAM_RGB     true

using namespace cv;
using namespace raspicam;

class PiCam
{
private:
    RaspiCam_Cv camera;
    Mat image;
    vector<uchar> buf;
    Mat lastFrame, currentFrame, nextFrame;

public:
    bool setup();
    void close();
    Mat getFrame();
    Mat getFrameMotionDetection();
    vector<uchar> encodeFrame(Mat frame);
};