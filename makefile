debug = -g
warnings = -Wall

camera:
	g++ $(warnings) $(debug) camera.cpp lib/lib.cpp lib/picam.cpp lib/server.cpp -o bin/camera -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui

controller:
	g++ $(warnings) $(debug) -lwiringPi controller.cpp lib/lib.cpp lib/rover.cpp lib/server.cpp -o bin/controller

lights:
	g++ $(warnings) $(debug) lights.cpp lib/lib.cpp lib/gpio.cpp -o bin/lights

broadcast:
	g++ $(warnings) $(debug) broadcast.cpp lib/lib.cpp lib/broadcast.cpp -o bin/broadcast