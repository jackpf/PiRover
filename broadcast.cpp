#include "lib/lib.hpp"
#include "lib/broadcast.hpp"
#include "lib/network.hpp"

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
    WLANSstatus wlan;

    if (args.count() > 0) { /* Broadcast as client */
        printf("Resolved IP: %s\n", broadcaster.resolve(args.get(0)));
        exit(0);
    } else {
        printf("Listening for broadcasts\n");

        // Generate return string
        const char *format = "PiRover;control:%s;camera:%s;";
        int len = snprintf(NULL, 0, format, args.get("ctrlport"), args.get("camport"));
        char returnStr[len + 1];
        snprintf(returnStr, len + 1, format, args.get("ctrlport"), args.get("camport"));

        broadcaster.listen(HANDSHAKE, returnStr);
    }

    return 0;
}