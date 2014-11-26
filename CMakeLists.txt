# Main CMake file

cmake_minimum_required(VERSION 2.8.4)
project(PiRover)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x")

set(LIB_DIR "${PROJECT_SOURCE_DIR}/src/lib")

include_directories(${LIB_DIR})
add_subdirectory("${PROJECT_SOURCE_DIR}/lib/raspicam/")
add_subdirectory("${PROJECT_SOURCE_DIR}/src/")
add_subdirectory("${PROJECT_SOURCE_DIR}/tests/" EXCLUDE_FROM_ALL)
