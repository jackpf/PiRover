controller:
	gcc -lgtest -lgtest_main -lpthread -lwiringPi controller.cpp ../lib/rover.cpp -o bin/controller

gpio:
	gcc -lgtest -lgtest_main -lpthread gpio.cpp ../lib/gpio.cpp -o bin/gpio

camera:
	gcc -lgtest -lgtest_main -lpthread camera.cpp ../lib/picam.cpp -o bin/camera -I/usr/local/include/ -lraspicam -lraspicam_cv -lopencv_core -lopencv_highgui