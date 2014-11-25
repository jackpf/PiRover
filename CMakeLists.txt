cmake_minimum_required(VERSION 2.8.4)
project(PiRover)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x")

include_directories("lib/")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/bin/")

add_executable(camera camera.cpp lib/lib.cpp lib/server.cpp lib/picam.cpp)
target_link_libraries(camera raspicam raspicam_cv opencv_core opencv_highgui)

add_executable(controller controller.cpp lib/lib.cpp lib/server.cpp lib/gpio.cpp lib/rover.cpp)
target_link_libraries(controller pthread)

add_executable(lights lights.cpp lib/lib.cpp lib/gpio.cpp lib/network.cpp)
target_link_libraries(lights pthread)

add_executable(broadcast broadcast.cpp lib/lib.cpp lib/broadcast.cpp)

install(TARGETS camera controller lights broadcast DESTINATION /usr/local/bin/)
install(FILES pirover DESTINATION /etc/init.d PERMISSIONS OWNER_EXECUTE OWNER_WRITE OWNER_READ)
execute_process(COMMAND update-rc.d pirover defaults
                RESULT_VARIABLE Result
                OUTPUT_VARIABLE Output
                ERROR_VARIABLE Error)