debug = -g
warnings = -Wall

camera:
	g++ $(warnings) $(debug) camera.cpp lib/picam.cpp lib/server.cpp -o bin/camera -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui

controller:
	g++ $(warnings) $(debug) -lwiringPi controller.cpp lib/rover.cpp lib/server.cpp -o bin/controller

lights:
	g++ $(warnings) $(debug) lights.cpp lib/gpio.cpp lib/lib.cpp -o bin/lights

broadcast:
	g++ $(warnings) $(debug) broadcast.cpp lib/broadcast.cpp -o bin/broadcast