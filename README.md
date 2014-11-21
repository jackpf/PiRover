PiRover
=======

Raspberry Pi + Android app controlled robotic vehicle :D

## Checkout:

Make sure to add the recursive flag to download the raspicam library also.

```
git clone --recursive http://github.com/jackpf/PiRover.git
```

## Build instructions (Raspberry Pi):

Install OpenCV libraries:

```
sudo apt-get install libopencv-core-dev
```

Install raspicam libraries:

```
cd PiRover/lib/raspicam
mkdir build
cd build
cmake ..
make
sudo make install
sudo ldconfig
```

Build PiRover:
```
cd PiRover
mkdir build
cd build
cmake ..
make
sudo make install
```

To start PiRover:
```
sudo service pirover start
```

To stop PiRover:
```
sudo service pirover stop
```

## Build instructions (Android App):

The Android app should be able to be built from eclipse with the Android Developer Tools plugin.
