#include "gpio.hpp"

#define LIGHT 11

int main(int argc, char *argv[])
{
    GPIO::setup();

    GPIO::pinMode(LIGHT, GPIO::OUT);

    int t = 1000000;

    if (argc > 1) {
        int tmp = atoi(argv[1]);
        if (tmp > 0) {
            t = tmp;
        }
    }

    while (true) {
        GPIO::write(LIGHT, GPIO::HIGH);
        usleep(t);
        GPIO::write(LIGHT, GPIO::LOW);
        usleep(t);
    }

    return 0;
}