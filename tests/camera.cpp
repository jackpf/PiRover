#include "gtest/gtest.h"
#include "picam.hpp"

TEST(Camera, OpenCamera)
{
    PiCam cam;

    EXPECT_TRUE(cam.setup());

    cam.close();
}

TEST(Camera, CaptureFrame)
{
    PiCam cam;

    cam.setup();

    EXPECT_GT(cam.getFrame().size(), 0);

    cam.close();
}