#include "server.hpp"

bool Server::create(int port)
{
    sock = socket(AF_INET, SOCK_STREAM, 0);

    if (sock < 0) {
        return false;
    }

    struct timeval timeout;
    timeout.tv_sec = 5;
    timeout.tv_usec = 0;

    //setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &timeout, sizeof(timeout));
    setsockopt(sock, SOL_SOCKET, SO_SNDTIMEO, &timeout, sizeof(timeout));

    memset(&sockAddress, '0', sizeof(sockAddress));
    sockAddress.sin_family = AF_INET;
    sockAddress.sin_addr.s_addr = htonl(INADDR_ANY);
    sockAddress.sin_port = htons(port); 

    if (::bind(sock, (struct sockaddr *) &sockAddress, sizeof(sockAddress)) < 0) {
        return false;
    }

    if (::listen(sock, 1) < 0) {
        return false;
    }

    return true;
}

int Server::listen()
{
    return ::accept(sock, (struct sockaddr *) NULL, NULL);
}

int Server::receive(int connection, void *data, int len)
{
    return ::read(connection, data, len);
}

int Server::send(int connection, void *data, int len)
{
    return ::send(connection, data, len, MSG_NOSIGNAL); 
}

int Server::close(int connection)
{
    return ::close(connection);
}
