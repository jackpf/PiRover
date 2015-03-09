#include "lib.hpp"
#include "picam.hpp"
#include "server.hpp"
#include <signal.h>

using namespace Lib;
using namespace cv;

static Args::ArgumentOptions options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};

static bool motionDetection = false;

static void signalHandler(int sigNum)
{
    Lib::println("Turning motion detection %s", motionDetection ? "off" : "on");
    motionDetection = !motionDetection;
}

int main(int argc, char **argv)
{
    signal(SIGUSR1, signalHandler);

    Args args(argc, argv, options, sizeof(options));

    Server server;
    PiCam cam;

    // Create server
    printf("Creating camera server\n");

    if (!server.create(atoi(args.get("port")))) {
        perror("Could not create server");
        exit(-1);
    }

    while (true) {
        println("Listening...");

        int conn = server.listen();

        println("Client connected\nSetting up camera");

        if (!cam.setup()) {
            println("Unable to open camera");
            server.close(conn);
            continue;
        }

        int status;
        vector<uchar> im;

        do {
            im = cam.encodeFrame(!motionDetection ? cam.getFrame() : cam.getFrameMotionDetection());

            // Send image size
            int sz[1] = {im.size()};
            status = server.send(conn, &sz, sizeof(int));

            // Send image data
            size_t sent = 0;
            do {
                sent += server.send(conn, &im[sent], im.size() - sent);
            } while (sent < im.size());

            //Lib::println("Sent %d bytes", sent);
        } while (status >= 0);

        println("Client disconnected");

        server.close(conn);

        println("Disconnecting camera");

        cam.close();
    }

    return 0;
}