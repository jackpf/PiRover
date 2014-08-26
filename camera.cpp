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
    ~PiCam()
    {
        if (camera.isOpened()) {
            camera.release();
        }
    }

    bool setup()
    {
        if (!camera.open()) {
            return false;
        }

        //camera.set(CV_CAP_PROP_FRAME_WIDTH, 640);
        //camera.set(CV_CAP_PROP_FRAME_HEIGHT, 480);

        // Warm up - seems like we need to get some frames so exposure and contrast and stuff are set up
        for (int i = 0; i < WARMUP_FRAMES; i++) {
            camera.grab();
        }

        return true;
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
        ::read(connection, data, 1024);

        return data;
    }

    void send(int connection, void *data, int len)
    {
        ::write(connection, data, len); 
    }

    void close(int connection)
    {
        ::close(connection);
    }
};
 
int main(int argc, char **argv)
{
    // Connect to camera
    PiCam cam;

    printf("Setting up camera\n");
    
    if (!cam.setup()) {
        printf("Unable to open camera\n");
        exit(-1);
    }

    // Create server
    PiCamServer server;

    printf("Creating server\n");

    if (!server.create(1337)) {
        printf("Could not create server: %s\n", strerror(errno));
    }

    printf("Listening...\n");

    int conn = server.listen();

    while(true) {
        char *data = (char *) server.receive(conn);
        printf("Received: '%s'\n", data);
        if (strcmp(data, "frame\n") == 0) {
            printf("Capturing frame\n");
            vector<uchar> buf = cam.getFrame();
            printf("Sending data\n");
            server.send(conn, &buf[0], buf.size());
            printf("Data sent\n");
        }
    }

    server.close(conn);

    return 0;
}