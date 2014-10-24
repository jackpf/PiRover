#include "lib/lib.hpp"
#include "lib/server.hpp"
#include "lib/rover.hpp"

static struct argp_option options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};
 
int main(int argc, char **argv)
{
    Lib::Args args(argc, argv, options, sizeof(options));

    Server server;
    Rover rover;

    // Setup rover
    printf("Initialising rover\n");

    rover.setup();

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

        while (true) {
            int accelerationPosition, steeringPosition;
            if (server.receive(conn, (void *) &accelerationPosition, sizeof(int)) <= 0) {
                break;
            }
            if (server.receive(conn, (void *) &steeringPosition, sizeof(int)) <= 0) {
                break;
            }

            printf("Steering: %d, Accel: %d\n", steeringPosition, accelerationPosition);

            rover.accelerate(
                rover.process(accelerationPosition, steeringPosition)
            );
        }

        printf("Closing server\n");

        server.close(conn);
    }

    return 0;
}