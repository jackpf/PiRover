#include <stdio.h>
#include <raspicam/raspicam_cv.h>

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <time.h>

using namespace cv;
using namespace raspicam;

#define WARMUP_FRAMES 10

class PiCam
{
private:
    RaspiCam_Cv camera;

public:
    bool setup()
    {
        camera.set(CV_CAP_PROP_FRAME_WIDTH, 320);
        camera.set(CV_CAP_PROP_FRAME_HEIGHT, 240);
        camera.set(CV_CAP_PROP_FORMAT, CV_8UC1);

        if (!camera.open()) {
            return false;
        }

        // Warm up - seems like we need to get some frames so exposure and contrast and stuff are set up
        for (int i = 0; i < WARMUP_FRAMES; i++) {
            camera.grab();
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
        Mat image;//(640, 480, CV_8UC1);
        vector<uchar> buf;

        camera.grab();
        camera.retrieve(image);
        imencode(".jpg", image, buf);

        return buf;
    }
};

class PiCamServer
{
private:
    int sock;
    struct sockaddr_in sockAddress;

public:
    bool create(int port)
    {
        sock = socket(AF_INET, SOCK_STREAM, 0);

        if (sock < 0) {
            return false;
        }

        memset(&sockAddress, '0', sizeof(sockAddress));
        sockAddress.sin_family = AF_INET;
        sockAddress.sin_addr.s_addr = htonl(INADDR_ANY);
        sockAddress.sin_port = htons(port); 

        if (::bind(sock, (struct sockaddr *) &sockAddress, sizeof(sockAddress)) < 0) {
            return false;
        }

        if (::listen(sock, 1) < 0) {
            return false;
        }

        return true;
    }

    int listen()
    {
        return ::accept(sock, (struct sockaddr *) NULL, NULL);
    }

    void *receive(int connection)
    {
        void *data = malloc(1024);
        if (::read(connection, data, 1024) < 0) {
            return NULL;
        }

        return data;
    }

    int send(int connection, void *data, int len)
    {
        return ::send(connection, data, len, MSG_NOSIGNAL); 
    }

    int close(int connection)
    {
        return ::close(connection);
    }
};
 
int main(int argc, char **argv)
{
    PiCamServer server;
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