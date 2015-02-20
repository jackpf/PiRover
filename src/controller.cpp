#include "lib.hpp"
#include "server.hpp"
#include "controllers/handler.hpp"
#include "controllers/rover_controller.hpp"
#include "controllers/launcher_controller.hpp"
#include "controllers/shutdown_controller.hpp"

using namespace Lib;

static Args::ArgumentOptions options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};
 
int main(int argc, char **argv)
{
    Args args(argc, argv, options, sizeof(options));

    Server server;

    Handler *handlers[] = {
        /* 0x0 */ new RoverController(),
        /* 0x1 */ new LauncherController(),
        /* 0x2 */ new ShutdownController(),
    };

    // Create server
    println("Creating controller server");

    if (!server.create(atoi(args.get("port")))) {
        perror("Could not create server");
        exit(-1);
    }

    while (true) {
        println("Listening...");

        int conn = server.listen();

        println("Client connected");

        while (true) {
            // Get cmd id
            int id;
            if (server.receive(conn, &id, sizeof(int)) <= 0) {
                break;
            }

            if (id >= sizeof(handlers) / sizeof(Handler)) {
                Lib::println(stderr, "Invalid command id of %d", id);
                continue;
            }

            char *data = (char *) malloc(handlers[id]->getDataSize());

            int totalBytesRead = 0;

            while (totalBytesRead < handlers[id]->getDataSize()) {
                int bytesRead = server.receive(conn, data + totalBytesRead, handlers[id]->getDataSize() - totalBytesRead);

                if (bytesRead <= 0) {
                    break;
                }

                totalBytesRead += bytesRead;
            }

            if (totalBytesRead < handlers[id]->getDataSize()) {
                break;
            }

            handlers[id]->handle(data);

            free(data);
        }

        println("Client disconnected");

        server.close(conn);

        println("Cleaning up handlers");

        for (int i = 0; i < sizeof(handlers) / sizeof(Handler); i++) {
            handlers[i]->cleanup();
        }
    }

    for (int i = 0; i < sizeof(handlers) / sizeof(Handler); i++) {
        delete handlers[i];
    }

    return 0;
}