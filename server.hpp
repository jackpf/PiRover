#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>

class Server
{
private:
    int sock;
    struct sockaddr_in sockAddress;

public:
    bool create(int port);
    int listen();
    int receive(int connection, void *data, int len);
    int send(int connection, void *data, int len);
    int close(int connection);
};