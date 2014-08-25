#include "rover.hpp"

int main(int argc, char *argv[])
{
	Rover *rover = new Rover();

	rover->setup();
	rover->process(argc, argv);

	return 0;
}