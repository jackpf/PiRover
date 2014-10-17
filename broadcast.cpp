#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>

#define PORT        2080
#define HANDSHAKE   "PiRover"

int main(int argc, char *argv[])
{
    int sock;
    struct sockaddr_in sendAddress;
    struct sockaddr_in receiveAddress;
    socklen_t addressLength;

    if ((sock = socket(PF_INET, SOCK_DGRAM, 0)) < 0) {
        perror("Create");
        exit(-1);
    }

    int broadcast = 1;
    setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast));

    printf("Listening for broadcasts\n");

    memset(&receiveAddress, 0, sizeof(receiveAddress));
    receiveAddress.sin_family = AF_INET;
    receiveAddress.sin_port = htons(PORT);
    receiveAddress.sin_addr.s_addr = INADDR_ANY;

    char buf[32];
    int bytes;

    if (argc > 1) { /* Broadcast as client */
        memset(&sendAddress, 0, sizeof(sendAddress));
        sendAddress.sin_family = AF_INET;
        sendAddress.sin_port = htons(PORT);
        sendAddress.sin_addr.s_addr = INADDR_BROADCAST;

        bytes = sendto(sock, argv[1], sizeof(argv[1]), 0, (struct sockaddr *) &sendAddress, sizeof sendAddress);
        printf("Broadcasted packet: '%s'\n", argv[1]);

        struct timeval tv;
        tv.tv_sec = 0;
        tv.tv_usec = 500000;
        setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv));

        bytes = recvfrom(sock, buf, sizeof(buf), 0, (struct sockaddr *) &sendAddress, &addressLength);

        if (bytes > -1) {
            char sendIP[INET_ADDRSTRLEN + 1];
            inet_ntop(AF_INET, &sendAddress.sin_addr.s_addr, sendIP, INET_ADDRSTRLEN);

            printf("Device address: %s\n", sendIP);
        } else {
            printf("Unable to determine device IP\n");
        }

        exit(0);
    } else {
        if (bind(sock, (struct sockaddr *) &receiveAddress, sizeof(receiveAddress)) < 0) {
            perror("Bind");
            exit(-1);
        }

        while (true) {
            addressLength = sizeof(sendAddress);
            if ((bytes = recvfrom(sock, buf, sizeof(buf), 0, (struct sockaddr *) &sendAddress, &addressLength)) > 0) {
                char sendIP[INET_ADDRSTRLEN + 1];
                inet_ntop(AF_INET, &sendAddress.sin_addr.s_addr, sendIP, INET_ADDRSTRLEN);

                printf("Received: %s from %s\n", buf, sendIP);

                if (strcmp(buf, HANDSHAKE) == 0) {
                    printf("Correct handshake\n");

                    sendto(sock, HANDSHAKE, sizeof(HANDSHAKE), 0, (struct sockaddr *) &sendAddress, sizeof(sendAddress));
                } else {
                    printf("Incorrect handshake\n");
                }
            } else {
                perror("Receive");
            }
        }
    }

    close(sock);

    return 0;
}