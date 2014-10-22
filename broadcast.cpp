#include "lib/broadcast.hpp"

#define PORT        2080
#define HANDSHAKE   "PiRover"

int main(int argc, char *argv[])
{
    Broadcast broadcaster;

    broadcaster.init(PORT);

    if (argc > 1) { /* Broadcast as client */
        printf("Resolved IP: %s\n", broadcaster.resolve(argv[1]));
        exit(0);
    } else {
        printf("Listening for broadcasts\n");
        broadcaster.listen(HANDSHAKE);
    }

    return 0;
}