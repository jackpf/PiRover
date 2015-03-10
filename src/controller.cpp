#include <handler.hpp>
#include "lib.hpp"
#include "server.hpp"
#include "handler_manager.hpp"

using namespace Lib;

static Args::ArgumentOptions options[] = {
    {"port", 'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {0}
};

int main(int argc, char **argv)
{
    Args args(argc, argv, options, sizeof(options));

    Server server;

    HandlerManager handlerManager(
        5,
        /* 0x0 */ new RoverController(),
        /* 0x1 */ new LauncherController(),
        /* 0x2 */ new ShutdownController(),
        /* 0x3 */ new SensorController(),
        /* 0x4 */ new CameraController()
    );

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

            Handler *handler = handlerManager.get(id);

            if (handler == NULL) {
                Lib::println(stderr, "Invalid command id of %d", id);
                continue;
            }

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

            Handler::ReturnData returnData = handler->handle(data);

            if (returnData.len > 0 && returnData.data != NULL) {
                server.send(conn, returnData.data, returnData.len);
            }

            free(data);
        }

        println("Client disconnected");

        server.close(conn);

        handlerManager.cleanup();
    }

    return 0;
}