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

Mat PiCam::getFrame()
{
    camera.grab();
    camera.retrieve(image);

    return image;
}

Mat PiCam::getFrameMotionDetection()
{
    if (!lastFrame.data) lastFrame = getFrame();
    if (!currentFrame.data) currentFrame = getFrame();
    if (!nextFrame.data) nextFrame = getFrame();

    lastFrame = currentFrame.clone();
    currentFrame = nextFrame.clone();
    nextFrame = getFrame();

    Mat d1, d2, result, rendered;

    absdiff(lastFrame, nextFrame, d1);
    absdiff(currentFrame, nextFrame, d2);
    bitwise_and(d1, d2, result);
    cvtColor(result, result, CV_BGR2GRAY);
    threshold(result, result, 35, 255, CV_THRESH_BINARY);
    erode(result, result, getStructuringElement(MORPH_RECT, Size(2,2)));

    int minX = -1, maxX = -1, minY = -1, maxY = -1, changes = 0;
    for(int i = 0; i < result.rows; i += 2) {
        for(int j = 0; j < result.cols; j += 2) {
            if(result.at<uchar>(i, j) == 255) {
                changes++;
                if(minX > j || minX == -1) minX = j;
                if(maxX < j || maxX == -1) maxX = j;
                if(minY > i || minY == -1) minY = i;
                if(maxY < i || maxY == -1) maxY = i;
            }
        }
    }

    rendered = currentFrame.clone();

    if (changes > 25) {
        rectangle(
            rendered,
            Point(minX, minY),
            Point(maxX, maxY),
            Scalar(0, 0, 230)
        );
    }

    return rendered;
}

vector<uchar> PiCam::encodeFrame(Mat frame)
{
    imencode(".jpg", frame, buf);

    return buf;
}