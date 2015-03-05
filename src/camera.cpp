#include "lib.hpp"
#include "picam.hpp"
#include "server.hpp"

using namespace Lib;

static Args::ArgumentOptions options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};

int main(int argc, char **argv)
{
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
        vector<uchar> frame, lastFrame;

        do {
            lastFrame = frame;
            frame = cam.getFrame();

            // Send image size
            int sz[1] = {frame.size()};
            status = server.send(conn, &sz, sizeof(int));

            // Send image data
            size_t sent = 0;
            do {
                sent += server.send(conn, &frame[sent], frame.size() - sent);
            } while (sent < frame.size());
        } while (status >= 0);

        println("Client disconnected");

        server.close(conn);

        println("Disconnecting camera");

        cam.close();
    }

    return 0;
}