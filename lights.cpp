#include "lib/lib.hpp"
#include "lib/gpio.hpp"
#include "lib/network.hpp"

#define LIGHT 11

static struct argp_option options[] = {
    {"interval", 'i', "INTERVAL", 0, "Interval to check connection status"},
    {0}
};

bool connected = false;

int main(int argc, char *argv[])
{
    Lib::Args args(argc, argv, options, sizeof(options));

    WLANStatus wlan;
    int t = atoi(args.get("interval", "30"));

    pthread_t thread;
    pthread_create(&thread, NULL, blinkThread, NULL);

    while (true) {
        connected = wlan.isConnected();
        sleep(t);
    }
}

void blinkThread(void *data)
{
    GPIO::setup();

    GPIO::pinMode(LIGHT, GPIO::OUT);

    int t = atoi(args.get("blink", ""));

    while (true) {
        GPIO::write(LIGHT, GPIO::HIGH);
        usleep(t);
        GPIO::write(LIGHT, GPIO::LOW);
        usleep(t);
    }

    return 0;
}