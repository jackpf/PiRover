#include "server.hpp"
#include "rover.hpp"
 
int main(int argc, char **argv)
{
    Server server;
    Rover rover;

    // Setup rover
    printf("Initialising rover\n");

    rover.setup();

    // Create server
    printf("Creating server\n");

    if (!server.create(1338)) {
        printf("Could not create server: %s\n", strerror(errno));
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

            rover.process(accelerationPosition, steeringPosition);
        }

        printf("Closing server\n");

        server.close(conn);
    }

    return 0;
}