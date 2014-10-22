#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>

#define DEBUG 0

class Broadcast
{
private:
    int sock;
    int port;
    struct sockaddr_in sendAddress;
    struct sockaddr_in receiveAddress;

    void debug();

public:
    void init(int port);

    void listen(const char *broadcast);

    char *resolve(const char *broadcast);

    void close();
};
