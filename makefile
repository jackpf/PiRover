debug = -g

all:  *.cpp
    g++ -Wall $(debug) -D_POSIX_C_SOURCE=199309L -D _BSD_SOURCE -lm -lwiringPi -lwiringPiDev -lpthread *.cpp ./lib/*.cpp -o ./bin/controller
