#include "lib.hpp"
#include "server.hpp"
#include "rover.hpp"

using namespace Lib;

static Args::ArgumentOptions options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};
 
int main(int argc, char **argv)
{
    Args args(argc, argv, options, sizeof(options));

    Server server;
    Rover rover;

    // Setup rover
    println("Initialising rover");

    rover.setup();

    // Create server
    println("Creating server");

    if (!server.create(atoi(args.get("port")))) {
        perror("Could not create server");
        exit(-1);
    }

    while (true) {
        println("Listening...");

        int conn = server.listen();

        println("Client connected");

        while (true) {
            int accelerationPosition, steeringPosition;
            if (server.receive(conn, (void *) &accelerationPosition, sizeof(int)) <= 0) {
                break;
            }
            if (server.receive(conn, (void *) &steeringPosition, sizeof(int)) <= 0) {
                break;
            }

            rover.calculateMotorValues(accelerationPosition, steeringPosition);
            rover.write();
        }

        println("Client disconnected");

        server.close(conn);

        println("Resetting rover");

        rover.resetMotorValues();
        rover.write();
    }

    return 0;
}