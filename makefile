debug = -g
warnings = -Wall

main:
    g++ $(warnings) $(debug) -D_POSIX_C_SOURCE=199309L -D _BSD_SOURCE -lm -lwiringPi -lwiringPiDev -lpthread *.cpp ./lib/*.cpp -o ./bin/controller

camera:
	g++ $(warnings) $(debug) camera.cpp server.cpp -o bin/camera -I/usr/local/include/ -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui

controller:
	g++ $(warnings) $(debug) -lwiringPi controller.cpp server.cpp rover.cpp -o bin/controller

lights:
	g++ $(warnings) $(debug) lights.cpp gpio.cpp -o bin/lights

broadcast:
	g++ $(warnings) $(debug) broadcast.cpp -o bin/broadcast