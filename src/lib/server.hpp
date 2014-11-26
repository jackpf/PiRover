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

    /**
     * Socket file descriptor
     */
    int sock;

    /**
     * Socket address info
     */
    struct sockaddr_in sockAddress;

public:

    /**
     * Create server
     *
     * @param port          Port number to bind socket to
     */
    bool create(int port);

    /**
     * Listen for incoming connections
     *
     * @return              Connection file descriptor
     */
    int listen();

    /**
     * Receive data
     *
     * @param connection    Connection file descriptor
     * @param data          Pointer to store data
     * @param len           Length of data to be read
     * @return              Bytes read
     */
    int receive(int connection, void *data, int len);

    /**
     * Send data
     *
     * @param connection    Connection file descriptor
     * @param data          Pointer to data to be sent
     * @param len           Length of data to be sent
     * @return              Bytes sent
     */
    int send(int connection, void *data, int len);

    /**
     * Close connection
     *
     * @param connection    Connection file descriptor
     * @return              Success
     */
    int close(int connection);
};