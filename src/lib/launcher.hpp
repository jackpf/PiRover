#include <usb.h>
#include <stdio.h>
#include <unistd.h>
#include "lib.hpp"

#define LAUNCHER_DEVICE_ID  0x2123
#define LAUNCHER_DOWN       0x01
#define LAUNCHER_UP         0x02
#define LAUNCHER_LEFT       0x04
#define LAUNCHER_RIGHT      0x08
#define LAUNCHER_FIRE       0x10
#define LAUNCHER_STOP       0x20

class Launcher
{
private:
    usb_dev_handle *launcher;
    bool connected;

public:
    bool init();
    void deinit();
    void send(int cmd);
};