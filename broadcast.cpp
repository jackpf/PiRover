#include "lib/lib.hpp"
#include "lib/broadcast.hpp"

#define PORT        2080
#define HANDSHAKE   "PiRover"

static struct argp_option options[] = {
    {"port",        'p', "PORT", OPTION_REQUIRED, "Port to listen to"},
    {"ctrlport",    'x', "PORT", OPTION_REQUIRED, "Port controller server is listening to"},
    {"camport",     'c', "PORT", OPTION_REQUIRED, "Port camera server is listening to"},
    {0}
};

int main(int argc, char *argv[])
{
    Lib::Args args(argc, argv, options, sizeof(options));

    Broadcast broadcaster(atoi(args.get("port")));

    if (args.count() > 0) { /* Broadcast as client */
        printf("Resolved IP: %s\n", broadcaster.resolve(args.get(0)));
        exit(0);
    } else {
        printf("Listening for broadcasts\n");

        // Generate return string
        char returnStr[64];
        snprintf(returnStr, 64, "PiRover;control:%s;camera:%s;", args.get("ctrlport"), args.get("camport"));

        broadcaster.listen(HANDSHAKE, returnStr);
    }

    return 0;
}