#include "lib/server.hpp"
#include "lib/rover.hpp"

#define PORT 1338
 
int main(int argc, char **argv)
{
    Server server;
    Rover rover;

    // Setup rover
    printf("Initialising rover\n");

    rover.setup();

    // Create server
    printf("Creating server\n");

    if (!server.create(PORT)) {
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

            rover.process(accelerationPosition, steeringPosition);
        }

        printf("Closing server\n");

        server.close(conn);
    }

    return 0;
}