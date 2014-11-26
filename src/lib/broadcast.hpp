#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include "lib.hpp"

class Broadcast
{
private:
    int sock;
    int port;
    struct sockaddr_in sendAddress;
    struct sockaddr_in receiveAddress;

    void debug();

public:
    Broadcast(int port);

    void listen(const char *handshake, const char *returnStr);

    char *resolve(const char *handshake);

    void close();
};
