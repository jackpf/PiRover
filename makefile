debug = -g
warnings = -Wall
std = c++0x

camera:
	g++ -std=$(std) $(warnings) $(debug) camera.cpp lib/lib.cpp lib/picam.cpp lib/server.cpp -o bin/camera -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui

controller:
	g++ -std=$(std) $(warnings) $(debug) -lwiringPi controller.cpp lib/lib.cpp lib/rover.cpp lib/server.cpp -o bin/controller

lights:
	g++ -std=$(std) $(warnings) $(debug) lights.cpp lib/lib.cpp lib/gpio.cpp -o bin/lights

broadcast:
	g++ -std=$(std) $(warnings) $(debug) broadcast.cpp lib/lib.cpp lib/broadcast.cpp -o bin/broadcast