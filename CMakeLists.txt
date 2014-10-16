cmake_minimum_required(VERSION 2.8.4)
project(PiRover)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    lib/lib.cpp
    lib/lib.hpp
    camera.cpp
    controller.cpp
    gpio.cpp
    gpio.hpp
    lights.cpp
    rover.cpp
    rover.hpp
    server.cpp
    server.hpp)

add_executable(PiRover ${SOURCE_FILES})