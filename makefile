debug = -g
warnings = -Wall

camera:
	g++ $(warnings) $(debug) camera.cpp lib/*.cpp -o bin/camera -I/usr/local/include/ -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui

controller:
	g++ $(warnings) $(debug) -lwiringPi controller.cpp lib/*.cpp -o bin/controller

lights:
	g++ $(warnings) $(debug) lights.cpp gpio.cpp -o bin/lights

broadcast:
	g++ $(warnings) $(debug) broadcast.cpp -o bin/broadcast