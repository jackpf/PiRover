# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/pi/workspace/PiRover/lib/PiCam

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/pi/workspace/PiRover/lib/PiCam

# Include any dependencies generated for this target.
include CMakeFiles/camcv.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/camcv.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/camcv.dir/flags.make

CMakeFiles/camcv.dir/RaspiCamControl.c.o: CMakeFiles/camcv.dir/flags.make
CMakeFiles/camcv.dir/RaspiCamControl.c.o: RaspiCamControl.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/pi/workspace/PiRover/lib/PiCam/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/camcv.dir/RaspiCamControl.c.o"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/camcv.dir/RaspiCamControl.c.o   -c /home/pi/workspace/PiRover/lib/PiCam/RaspiCamControl.c

CMakeFiles/camcv.dir/RaspiCamControl.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/camcv.dir/RaspiCamControl.c.i"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -E /home/pi/workspace/PiRover/lib/PiCam/RaspiCamControl.c > CMakeFiles/camcv.dir/RaspiCamControl.c.i

CMakeFiles/camcv.dir/RaspiCamControl.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/camcv.dir/RaspiCamControl.c.s"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -S /home/pi/workspace/PiRover/lib/PiCam/RaspiCamControl.c -o CMakeFiles/camcv.dir/RaspiCamControl.c.s

CMakeFiles/camcv.dir/RaspiCamControl.c.o.requires:
.PHONY : CMakeFiles/camcv.dir/RaspiCamControl.c.o.requires

CMakeFiles/camcv.dir/RaspiCamControl.c.o.provides: CMakeFiles/camcv.dir/RaspiCamControl.c.o.requires
	$(MAKE) -f CMakeFiles/camcv.dir/build.make CMakeFiles/camcv.dir/RaspiCamControl.c.o.provides.build
.PHONY : CMakeFiles/camcv.dir/RaspiCamControl.c.o.provides

CMakeFiles/camcv.dir/RaspiCamControl.c.o.provides.build: CMakeFiles/camcv.dir/RaspiCamControl.c.o

CMakeFiles/camcv.dir/RaspiCLI.c.o: CMakeFiles/camcv.dir/flags.make
CMakeFiles/camcv.dir/RaspiCLI.c.o: RaspiCLI.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/pi/workspace/PiRover/lib/PiCam/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/camcv.dir/RaspiCLI.c.o"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/camcv.dir/RaspiCLI.c.o   -c /home/pi/workspace/PiRover/lib/PiCam/RaspiCLI.c

CMakeFiles/camcv.dir/RaspiCLI.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/camcv.dir/RaspiCLI.c.i"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -E /home/pi/workspace/PiRover/lib/PiCam/RaspiCLI.c > CMakeFiles/camcv.dir/RaspiCLI.c.i

CMakeFiles/camcv.dir/RaspiCLI.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/camcv.dir/RaspiCLI.c.s"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -S /home/pi/workspace/PiRover/lib/PiCam/RaspiCLI.c -o CMakeFiles/camcv.dir/RaspiCLI.c.s

CMakeFiles/camcv.dir/RaspiCLI.c.o.requires:
.PHONY : CMakeFiles/camcv.dir/RaspiCLI.c.o.requires

CMakeFiles/camcv.dir/RaspiCLI.c.o.provides: CMakeFiles/camcv.dir/RaspiCLI.c.o.requires
	$(MAKE) -f CMakeFiles/camcv.dir/build.make CMakeFiles/camcv.dir/RaspiCLI.c.o.provides.build
.PHONY : CMakeFiles/camcv.dir/RaspiCLI.c.o.provides

CMakeFiles/camcv.dir/RaspiCLI.c.o.provides.build: CMakeFiles/camcv.dir/RaspiCLI.c.o

CMakeFiles/camcv.dir/RaspiPreview.c.o: CMakeFiles/camcv.dir/flags.make
CMakeFiles/camcv.dir/RaspiPreview.c.o: RaspiPreview.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/pi/workspace/PiRover/lib/PiCam/CMakeFiles $(CMAKE_PROGRESS_3)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/camcv.dir/RaspiPreview.c.o"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/camcv.dir/RaspiPreview.c.o   -c /home/pi/workspace/PiRover/lib/PiCam/RaspiPreview.c

CMakeFiles/camcv.dir/RaspiPreview.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/camcv.dir/RaspiPreview.c.i"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -E /home/pi/workspace/PiRover/lib/PiCam/RaspiPreview.c > CMakeFiles/camcv.dir/RaspiPreview.c.i

CMakeFiles/camcv.dir/RaspiPreview.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/camcv.dir/RaspiPreview.c.s"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -S /home/pi/workspace/PiRover/lib/PiCam/RaspiPreview.c -o CMakeFiles/camcv.dir/RaspiPreview.c.s

CMakeFiles/camcv.dir/RaspiPreview.c.o.requires:
.PHONY : CMakeFiles/camcv.dir/RaspiPreview.c.o.requires

CMakeFiles/camcv.dir/RaspiPreview.c.o.provides: CMakeFiles/camcv.dir/RaspiPreview.c.o.requires
	$(MAKE) -f CMakeFiles/camcv.dir/build.make CMakeFiles/camcv.dir/RaspiPreview.c.o.provides.build
.PHONY : CMakeFiles/camcv.dir/RaspiPreview.c.o.provides

CMakeFiles/camcv.dir/RaspiPreview.c.o.provides.build: CMakeFiles/camcv.dir/RaspiPreview.c.o

CMakeFiles/camcv.dir/camcv.c.o: CMakeFiles/camcv.dir/flags.make
CMakeFiles/camcv.dir/camcv.c.o: camcv.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/pi/workspace/PiRover/lib/PiCam/CMakeFiles $(CMAKE_PROGRESS_4)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/camcv.dir/camcv.c.o"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/camcv.dir/camcv.c.o   -c /home/pi/workspace/PiRover/lib/PiCam/camcv.c

CMakeFiles/camcv.dir/camcv.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/camcv.dir/camcv.c.i"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -E /home/pi/workspace/PiRover/lib/PiCam/camcv.c > CMakeFiles/camcv.dir/camcv.c.i

CMakeFiles/camcv.dir/camcv.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/camcv.dir/camcv.c.s"
	/usr/bin/gcc  $(C_DEFINES) $(C_FLAGS) -S /home/pi/workspace/PiRover/lib/PiCam/camcv.c -o CMakeFiles/camcv.dir/camcv.c.s

CMakeFiles/camcv.dir/camcv.c.o.requires:
.PHONY : CMakeFiles/camcv.dir/camcv.c.o.requires

CMakeFiles/camcv.dir/camcv.c.o.provides: CMakeFiles/camcv.dir/camcv.c.o.requires
	$(MAKE) -f CMakeFiles/camcv.dir/build.make CMakeFiles/camcv.dir/camcv.c.o.provides.build
.PHONY : CMakeFiles/camcv.dir/camcv.c.o.provides

CMakeFiles/camcv.dir/camcv.c.o.provides.build: CMakeFiles/camcv.dir/camcv.c.o

# Object files for target camcv
camcv_OBJECTS = \
"CMakeFiles/camcv.dir/RaspiCamControl.c.o" \
"CMakeFiles/camcv.dir/RaspiCLI.c.o" \
"CMakeFiles/camcv.dir/RaspiPreview.c.o" \
"CMakeFiles/camcv.dir/camcv.c.o"

# External object files for target camcv
camcv_EXTERNAL_OBJECTS =

camcv: CMakeFiles/camcv.dir/RaspiCamControl.c.o
camcv: CMakeFiles/camcv.dir/RaspiCLI.c.o
camcv: CMakeFiles/camcv.dir/RaspiPreview.c.o
camcv: CMakeFiles/camcv.dir/camcv.c.o
camcv: CMakeFiles/camcv.dir/build.make
camcv: /opt/vc/lib/libmmal_core.so
camcv: /opt/vc/lib/libmmal_util.so
camcv: /opt/vc/lib/libmmal_vc_client.so
camcv: /opt/vc/lib/libvcos.so
camcv: /opt/vc/lib/libbcm_host.so
camcv: CMakeFiles/camcv.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking C executable camcv"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/camcv.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/camcv.dir/build: camcv
.PHONY : CMakeFiles/camcv.dir/build

CMakeFiles/camcv.dir/requires: CMakeFiles/camcv.dir/RaspiCamControl.c.o.requires
CMakeFiles/camcv.dir/requires: CMakeFiles/camcv.dir/RaspiCLI.c.o.requires
CMakeFiles/camcv.dir/requires: CMakeFiles/camcv.dir/RaspiPreview.c.o.requires
CMakeFiles/camcv.dir/requires: CMakeFiles/camcv.dir/camcv.c.o.requires
.PHONY : CMakeFiles/camcv.dir/requires

CMakeFiles/camcv.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/camcv.dir/cmake_clean.cmake
.PHONY : CMakeFiles/camcv.dir/clean

CMakeFiles/camcv.dir/depend:
	cd /home/pi/workspace/PiRover/lib/PiCam && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/pi/workspace/PiRover/lib/PiCam /home/pi/workspace/PiRover/lib/PiCam /home/pi/workspace/PiRover/lib/PiCam /home/pi/workspace/PiRover/lib/PiCam /home/pi/workspace/PiRover/lib/PiCam/CMakeFiles/camcv.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/camcv.dir/depend

