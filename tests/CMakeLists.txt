set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "${PROJECT_SOURCE_DIR}/tests/bin/")

# Build camera
add_executable(camera_test camera.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/server.cpp ${LIB_DIR}/picam.cpp)
target_link_libraries(camera_test raspicam raspicam_cv opencv_core opencv_highgui gtest gtest_main)

# Build controller
add_executable(controller_test controller.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/server.cpp ${LIB_DIR}/gpio.cpp ${LIB_DIR}/rover.cpp)
target_link_libraries(controller_test pthread gtest gtest_main)

# Build gpio
add_executable(gpio_test gpio.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/gpio.cpp)
target_link_libraries(gpio_test pthread gtest gtest_main)

# Build broadcast
add_executable(broadcast_test broadcast.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/broadcast.cpp)
target_link_libraries(broadcast_test pthread gtest gtest_main)

# Build pwm
add_executable(pwm_test pwm.cpp ${LIB_DIR}/lib.cpp ${LIB_DIR}/gpio.cpp ${LIB_DIR}/rover.cpp)
target_link_libraries(pwm_test pthread gtest gtest_main)