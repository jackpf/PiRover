#include "lib.hpp"
#include "broadcast.hpp"

#define PORT        2080
#define HANDSHAKE   "PiRover"

using namespace Lib;

static Args::ArgumentOptions options[] = {
    {"port",        'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {"ctrlport",    'x', "PORT", OPTION_REQUIRED, "Port controller server is listening to"},
    {"camport",     'c', "PORT", OPTION_REQUIRED, "Port camera server is listening to"},
    {0}
};

int main(int argc, char *argv[])
{
    Args args(argc, argv, options, sizeof(options));

    Broadcast broadcaster(atoi(args.get("port")));

    if (args.count() > 0) { /* Broadcast as client */
        println("Resolved IP: %s", broadcaster.resolve(args.get(0)));
        exit(0);
    } else {
        println("Listening for broadcasts");

        // Generate return string
        const char *format = "PiRover;control:%s;camera:%s;";
        int len = snprintf(NULL, 0, format, args.get("ctrlport"), args.get("camport"));
        char returnStr[len + 1];
        snprintf(returnStr, len + 1, format, args.get("ctrlport"), args.get("camport"));

        broadcaster.listen(HANDSHAKE, returnStr);
    }

    return 0;
}