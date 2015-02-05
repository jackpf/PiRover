#include "lib.hpp"
#include "server.hpp"
#include "controllers/handler.hpp"
#include "controllers/rover_controller.hpp"

using namespace Lib;

static Args::ArgumentOptions options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};
 
int main(int argc, char **argv)
{
    Args args(argc, argv, options, sizeof(options));

    Server server;

    //Handler *handlers[] = {new RoverController()};
    Handler *handler = new RoverController();

    // Setup rover
    println("Initialising rover");

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
            char *data = (char *) malloc(handler->getDataSize());

            int totalBytesRead = 0;

            while (totalBytesRead < handler->getDataSize()) {
                int bytesRead = server.receive(conn, data + totalBytesRead, handler->getDataSize() - totalBytesRead);

                if (bytesRead <= 0) {
                    break;
                }

                totalBytesRead += bytesRead;
            }

            if (totalBytesRead < handler->getDataSize()) {
                break;
            }

            handler->handle(data);

            //free(data);
        }

        println("Client disconnected");

        server.close(conn);

        println("Resetting rover");

        //rover.resetMotorValues();
        //rover.write();
    }

    return 0;
}