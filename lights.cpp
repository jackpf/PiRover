#include "gpio.hpp"

int main(int argc, char *argv[])
{
    GPIO::setup();

    GPIO::pinMode(11, GPIO::OUT);

    while (true) {
        GPIO::write(11, GPIO::HIGH);
        sleep(1);
        GPIO::write(11, GPIO::LOW);
        sleep(1);
    }

    return 0;
}