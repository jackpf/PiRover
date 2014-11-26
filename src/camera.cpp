#include "lib.hpp"
#include "picam.hpp"
#include "server.hpp"

static struct argp_option options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};

int main(int argc, char **argv)
{
    Lib::Args args(argc, argv, options, sizeof(options));

    Server server;
    PiCam cam;

    // Create server
    printf("Creating server\n");

    if (!server.create(atoi(args.get("port")))) {
        perror("Could not create server");
        exit(-1);
    }

    while (true) {
        printf("Listening...\n");

        int conn = server.listen();

        printf("Client connected\nSetting up camera\n");

        if (!cam.setup()) {
            printf("Unable to open camera\n");
            server.close(conn);
            continue;
        }

        int status;

        do {
            vector<uchar> buf = cam.getFrame();

            // Send image size
            int sz[1] = {buf.size()};
            status = server.send(conn, &sz, sizeof(int));

            // Send image data
            size_t sent = 0;
            do {
                sent += server.send(conn, &buf[sent], buf.size() - sent);
            } while (sent < buf.size());
        } while (status >= 0);

        printf("Client disconnected\n");

        server.close(conn);

        printf("Disconnecting camera\n");

        cam.close();
    }

    return 0;
}