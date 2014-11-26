#include "broadcast.hpp"

Broadcast::Broadcast(int port)
{
    this->port = port;

    if ((sock = socket(PF_INET, SOCK_DGRAM, 0)) < 0) {
        perror("Create");
        exit(-1);
    }

    int broadcast = 1;
    setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast));

    memset(&receiveAddress, 0, sizeof(receiveAddress));
    receiveAddress.sin_family = AF_INET;
    receiveAddress.sin_port = htons(port);
    receiveAddress.sin_addr.s_addr = INADDR_ANY;
}

void Broadcast::listen(const char *handshake, const char *returnStr)
{
    if (bind(sock, (struct sockaddr *) &receiveAddress, sizeof(receiveAddress)) < 0) {
        perror("Bind");
        exit(-1);
    }

    char buf[32];
    int bytes;

    while (true) {
        socklen_t addressLength = sizeof(sendAddress);
        if ((bytes = recvfrom(sock, buf, sizeof(buf), 0, (struct sockaddr *) &sendAddress, &addressLength)) > 0) {
            char sendIP[INET_ADDRSTRLEN + 1];
            inet_ntop(AF_INET, &sendAddress.sin_addr.s_addr, sendIP, INET_ADDRSTRLEN);

            Lib::println("Received: %s from %s", buf, sendIP);

            if (strcmp(buf, handshake) == 0) {
                Lib::println("Correct handshake");

                sendto(sock, returnStr, strlen(returnStr) + 1, 0, (struct sockaddr *) &sendAddress, sizeof(sendAddress));
            } else {
                Lib::println("Incorrect handshake");
            }
        } else {
            perror("Receive");
        }
    }
}

char *Broadcast::resolve(const char *handshake)
{
    memset(&sendAddress, 0, sizeof(sendAddress));
    sendAddress.sin_family = AF_INET;
    sendAddress.sin_port = htons(port);
    sendAddress.sin_addr.s_addr = INADDR_BROADCAST;

    char buf[32];
    int bytes;

    bytes = sendto(sock, handshake, strlen(handshake) + 1, 0, (struct sockaddr *) &sendAddress, sizeof sendAddress);
    Lib::println("Broadcasted packet: '%s'", handshake);

    struct timeval tv;
    tv.tv_sec = 0;
    tv.tv_usec = 500000;
    setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv));

    socklen_t addressLength = sizeof(sendAddress);
    bytes = recvfrom(sock, buf, sizeof(buf), 0, (struct sockaddr *) &sendAddress, &addressLength);

    if (bytes > -1) {
        char *sendIP = (char *) malloc((INET_ADDRSTRLEN + 1) * sizeof(char));
        inet_ntop(AF_INET, &sendAddress.sin_addr.s_addr, sendIP, INET_ADDRSTRLEN);

        return sendIP;
    } else {
        return NULL;
    }
}

void Broadcast::close()
{
    ::close(sock);
}
