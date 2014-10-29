debug = -g
warnings = -Wall
std = c++0x

controller:
	gcc -std=$(std) -lgtest -lgtest_main -lpthread -lwiringPi controller.cpp ../lib/rover.cpp -o bin/controller

gpio:
	gcc -std=$(std) -lgtest -lgtest_main -lpthread gpio.cpp ../lib/gpio.cpp -o bin/gpio

camera:
	gcc -std=$(std) -lgtest -lgtest_main -lpthread camera.cpp ../lib/picam.cpp -o bin/camera -I/usr/local/include/ -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui

broadcast:
	gcc -std=$(std) -lgtest -lgtest_main -lpthread broadcast.cpp ../lib/broadcast.cpp -o bin/broadcast