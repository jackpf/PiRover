#include "lib/lib.hpp"
#include "lib/gpio.hpp"
#include "lib/network.hpp"

#define LIGHT 11

static struct argp_option options[] = {
    {"interval", 'i', "INTERVAL", 0, "Interval in seconds to check connection status"},
    {0}
};

bool isConnected = false;

void *blinkThread(void *);

int main(int argc, char *argv[])
{
    Lib::Args args(argc, argv, options, sizeof(options));

    pthread_t thread;
    pthread_create(&thread, NULL, blinkThread, NULL);

    WLANStatus wlan;
    int t = atoi(args.get("interval", "10"));

    while (true) {
        isConnected = wlan.isConnected();
        sleep(t);
    }
}

void *blinkThread(void *data)
{
    GPIO::setup();

    GPIO::pinMode(LIGHT, GPIO::OUT);

    while (true) {
        int t = isConnected ? 100000 : 1000000;
        GPIO::write(LIGHT, GPIO::HIGH);
        usleep(t);
        GPIO::write(LIGHT, GPIO::LOW);
        usleep(t);
    }

    return NULL;
}
