#include "lib/lib.hpp"
#include "lib/picam.hpp"
#include "lib/server.hpp"

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
                size_t sent = 0;
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