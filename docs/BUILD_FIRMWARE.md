PiRover
=======

Building has been tested on a Raspberry Pi model B running Raspbian.

## Checkout:

Make sure to add the recursive flag to download the raspicam library also.

```
git clone --recursive http://github.com/jackpf/PiRover.git
```

## Build instructions:

Install OpenCV libraries:

```
sudo apt-get install libopencv-dev
```

Install USB libraries:

```
sudo apt-get install libusb-dev
```

Install PiRover:

```
cd PiRover
mkdir build
cd build
cmake ..
sudo make install
sudo ldconfig
```

The PiRover software should now start at boot. To start and stop the services run:

```
sudo service pirover start|stop|restart
```