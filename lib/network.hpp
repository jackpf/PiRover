#include <unistd.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <ifaddrs.h>
#include <stdio.h>
#include <string.h>
#include <linux/wireless.h>
#include <sys/ioctl.h>
#include <errno.h>
#include <math.h>
#include <stdbool.h>

class WLANStatus
{
private:
    int socket;
    struct ifaddrs *interface;

    bool supportsWlan(struct ifaddrs *interface);
    void findWlanInterface();

public:
    WLAN();

    void isConnected();
};