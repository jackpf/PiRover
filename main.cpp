#include "rover.hpp"

int main(int argc, char *argv[])
{
    Rover *rover = new Rover();

    rover->setup();

    while (true) {
        int c = getchar(); // Get char
        getchar(); // Consume return key
        if (c == 27) { // Special
            c = getchar(); // Get actual char
            getchar(); // Consume another?
        }

        rover->process(c);
    }

    return 0;
}