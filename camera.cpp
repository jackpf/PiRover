#include <stdio.h>
#include <raspicam/raspicam_cv.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>

#include "server.hpp"

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
    bool setup()
    {
        camera.set(CV_CAP_PROP_FRAME_WIDTH, CAM_WIDTH);
        camera.set(CV_CAP_PROP_FRAME_HEIGHT, CAM_HEIGHT);
        camera.set(CV_CAP_PROP_FORMAT, CAM_RGB ? CV_8UC3 : CV_8UC1);

        if (!camera.open()) {
            return false;
        }

        return true;
    }

    void close()
    {
        if (camera.isOpened()) {
            camera.release();
        }
    }

    vector<uchar> getFrame()
    {
        Mat image(CAM_WIDTH, CAM_HEIGHT, CAM_RGB ? CV_8UC3 : CV_8UC1);
        vector<uchar> buf;

        camera.grab();
        camera.retrieve(image);
        imencode(".jpg", image, buf);

        return buf;
    }
};


 
int main(int argc, char **argv)
{
    Server server;
    PiCam cam;

    // Create server
    printf("Creating server\n");

    if (!server.create(1337)) {
        printf("Could not create server: %s\n", strerror(errno));
        exit(-1);
    }

    while (true) {
        printf("Listening...\n");

        int conn = server.listen();

        printf("Client connected\n");

        printf("Setting up camera\n");
    
        if (!cam.setup()) {
            printf("Unable to open camera\n");
            server.close(conn);
            continue;
        }

        int status;

        do {
            //char *data = (char *) server.receive(conn);
            //printf("Received data: %s\n", data);

            //if (strcmp(data, "frame") == 0) {
                printf("Capturing frame\n");
                vector<uchar> buf = cam.getFrame();

                printf("Sending data\n");

                // Send image size
                int sent = 0;
                int sz[1] = {buf.size()};
                status = server.send(conn, &sz, sizeof(int));

                // Send image data
                do {
                    sent += server.send(conn, &buf[sent], buf.size() - sent);
                    printf("Sent %d bytes\n", sent);
                } while (sent < buf.size());
            //}
        } while (status >= 0);

        printf("Disconnecting camera\n");

        cam.close();

        printf("Closing server\n");

        server.close(conn);
    }

    return 0;
}