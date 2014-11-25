#include "network.hpp"

WLANStatus::WLANStatus()
{
    socket = ::socket(AF_INET, SOCK_DGRAM, 0);
}

bool WLANStatus::supportsWlan(struct ifaddrs *ifa)
{
    struct iwreq wrq;

    memset(&wrq, 0, sizeof(struct iwreq));
    strncpy(wrq.ifr_name, ifa->ifa_name, IFNAMSIZ);

    return ioctl(socket, SIOCGIWNAME, &wrq) == 0;
}

struct ifaddrs *WLANStatus::findWlanInterface()
{
    struct ifaddrs *ifap, *ifa;

    getifaddrs(&ifap);

    for (ifa = ifap; ifa; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr != NULL && ifa->ifa_addr->sa_family == AF_INET && supportsWlan(ifa)) {
            freeifaddrs(ifap);
            return ifa;
        }
    }

    freeifaddrs(ifap);

    return NULL;
}

bool WLANStatus::isConnected()
{
    struct ifaddrs *interface = findWlanInterface();

    if (interface == NULL) {
        return false;
    }
    
    // Not sure if we really need to check this?
    char *ip = (char *) malloc(20);
    struct sockaddr_in *sa = (struct sockaddr_in *) interface->ifa_addr;
    strncpy(ip, inet_ntoa(sa->sin_addr), 20);

    bool isConnected = strlen(ip) > 0;

    free(ip);

    return isConnected;
}
