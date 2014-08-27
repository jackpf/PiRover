debug = -g

main:
    g++ -Wall $(debug) -D_POSIX_C_SOURCE=199309L -D _BSD_SOURCE -lm -lwiringPi -lwiringPiDev -lpthread *.cpp ./lib/*.cpp -o ./bin/controller

camera:
	g++ camera.cpp -o bin/camera -I/usr/local/include/ -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui