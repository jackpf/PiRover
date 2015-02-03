set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/bin/")

# Build camera
add_executable(camera camera.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/server.cpp ${LIB_DIR}/picam.cpp)
target_link_libraries(camera raspicam raspicam_cv opencv_core opencv_highgui)

# Build controller
add_executable(controller controller.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/server.cpp ${LIB_DIR}/gpio.cpp ${LIB_DIR}/rover.cpp)
target_link_libraries(controller pthread)

# Build lights
add_executable(lights lights.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/gpio.cpp ${LIB_DIR}/network.cpp)
target_link_libraries(lights pthread)

# Build broadcast
add_executable(broadcast broadcast.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/broadcast.cpp)

# Build launcher
add_executable(launcher lib/launcher.cpp ${LIB_DIR}/lib.cpp)
target_link_libraries(launcher usb)

# Install targets
install(TARGETS camera controller lights broadcast DESTINATION /usr/local/bin/)
install(FILES ${PROJECT_SOURCE_DIR}/scripts/pirover DESTINATION /etc/init.d PERMISSIONS OWNER_EXECUTE OWNER_WRITE OWNER_READ)
install(FILES ${PROJECT_SOURCE_DIR}/scripts/wifi DESTINATION /boot PERMISSIONS OWNER_EXECUTE OWNER_WRITE OWNER_READ)
execute_process(COMMAND update-rc.d pirover defaults
                RESULT_VARIABLE Result
                OUTPUT_VARIABLE Output
                ERROR_VARIABLE Error)