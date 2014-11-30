#include "picam.hpp"

bool PiCam::setup()
{
    camera.set(CV_CAP_PROP_FRAME_WIDTH, CAM_WIDTH);
    camera.set(CV_CAP_PROP_FRAME_HEIGHT, CAM_HEIGHT);
    camera.set(CV_CAP_PROP_FORMAT, CAM_RGB ? CV_8UC3 : CV_8UC1);

    if (!camera.open()) {
        return false;
    }

    Mat image(CAM_WIDTH, CAM_HEIGHT, CAM_RGB ? CV_8UC3 : CV_8UC1);

    return true;
}

void PiCam::close()
{
    if (camera.isOpened()) {
        camera.release();
    }
}

vector<uchar> PiCam::getFrame()
{
    camera.grab();
    camera.retrieve(image);
    imencode(".jpg", image, buf);

    return buf;
}