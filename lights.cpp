#include "lib/gpio.hpp"
#include "lib/lib.hpp"

#define LIGHT 11

static struct argp_option options[] = {
    {"blink", 'b', "BLINKSPEED", 0, "LED blink speed in microseconds"},
    {0}
};

int main(int argc, char *argv[])
{
    Lib::Args args;
    args.parse(argc, argv, options, sizeof(options));

    GPIO::setup();

    GPIO::pinMode(LIGHT, GPIO::OUT);

    int t = atoi(args.get("blink", "1000000"));

    while (true) {
        GPIO::write(LIGHT, GPIO::HIGH);
        usleep(t);
        GPIO::write(LIGHT, GPIO::LOW);
        usleep(t);
    }

    return 0;
}