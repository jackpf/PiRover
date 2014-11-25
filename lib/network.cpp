#include "network.h"

WLAN::WLAN()
{
    sock = socket(AF_INET, SOCK_DGRAM, 0);
}

bool WLAN::supportsWlan(struct ifaddrs *interface)
{
    struct iwreq wrq;

    memset(&wrq, 0, sizeof(struct iwreq));
    strncpy(wrq.ifr_name, ifname, IFNAMSIZ);

    return ioctl(skfd, SIOCGIWNAME, &wrq) == 0;
}

bool WLAN::findWlanInterface()
{
    struct ifaddrs *ifap, *ifa;

    if (getifaddrs(&ifap) < 0) {
        return false;
    }

    for (ifa = ifap; ifa; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr != NULL
            && (ifa->ifa_addr->sa_family == AF_INET || ifa->ifa_addr->sa_family == AF_INET6)
            && supports_wlan(ifa)) {
            freeifaddrs(ifap);
            interface = ifap;
            return true;
        }
    }

    freeifaddrs(ifap);

    return false;
}

bool WLAN::isConnected()
{
    if (interface == NULL) {
        if (!findWlanInterface()) {
            return false;
        }
    }

    char *ip = (char *) malloc(20);
    struct sockaddr_in *sa = (struct sockaddr_in *) ifa->ifa_addr;
    ip = inet_ntoa(sa->sin_addr);

    bool isConnected = strlen(ip) > 0;

    free(ip);

    return isConnected;
}