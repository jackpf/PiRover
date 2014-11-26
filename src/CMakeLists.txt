# Build camera
add_executable(camera camera.cpp lib/lib.cpp lib/server.cpp lib/picam.cpp)
target_link_libraries(camera raspicam raspicam_cv opencv_core opencv_highgui)

# Build controller
add_executable(controller controller.cpp lib/lib.cpp lib/server.cpp lib/gpio.cpp lib/rover.cpp)
target_link_libraries(controller pthread)

# Build lights
add_executable(lights lights.cpp lib/lib.cpp lib/gpio.cpp lib/network.cpp)
target_link_libraries(lights pthread)

# Build broadcast
add_executable(broadcast broadcast.cpp lib/lib.cpp lib/broadcast.cpp)

# Install targets
install(TARGETS camera controller lights broadcast DESTINATION /usr/local/bin/)
install(FILES ${PROJECT_SOURCE_DIR}/scripts/pirover DESTINATION /etc/init.d PERMISSIONS OWNER_EXECUTE OWNER_WRITE OWNER_READ)
install(FILES ${PROJECT_SOURCE_DIR}/scripts/wifi DESTINATION /boot PERMISSIONS OWNER_EXECUTE OWNER_WRITE OWNER_READ)
execute_process(COMMAND update-rc.d pirover defaults
                RESULT_VARIABLE Result
                OUTPUT_VARIABLE Output
                ERROR_VARIABLE Error)