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
    struct ifaddrs *ifap, *ifa, *ifar = (struct ifaddrs *) malloc(sizeof(struct ifaddrs));

    getifaddrs(&ifap);

    for (ifa = ifap; ifa; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr != NULL && ifa->ifa_addr->sa_family == AF_INET && supportsWlan(ifa)) {
            memcpy(ifar, ifa, sizeof(struct ifaddrs));
            break;
        }
    }

    freeifaddrs(ifap);

    return ifar;
}

bool WLANStatus::isConnected()
{
    struct ifaddrs *interface = findWlanInterface();

    if (interface == NULL) {
        return false;
    }

    // Not sure if we really need to check this?
    struct sockaddr_in sa;
    memset(&sa, 0, sizeof(sa));
    memcpy(&sa, interface->ifa_addr, sizeof(struct sockaddr));
    struct in_addr in = sa.sin_addr;

    char *ip = inet_ntoa(in);

    free(interface);

    return strlen(ip) > 0;
}
