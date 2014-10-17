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

    if (bind(sock, (struct sockaddr *) &receiveAddress, sizeof(receiveAddress)) < 0) {
        perror("Bind");
        exit(-1);
    }

    char buf[32];
    int bytes;

    if (argc > 1) { /* Broadcast as client */
        char msg[] = "PiRover";
        numbytes = sendto(sockfd, msg, sizeof(msg), 0, (struct sockaddr *) &sendaddr, sizeof sendaddr);
        printf("Broadcasted packet: '%s'\n", msg);
        exit(0);
    } else {
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