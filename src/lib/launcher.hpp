#include <usb.h>
#include <stdio.h>
#include <unistd.h>
#include "lib.hpp"

#define DEVICE_ID   0x2123
#define CMD_DOWN    0x01
#define CMD_UP      0x02
#define CMD_LEFT    0x04
#define CMD_RIGHT   0x08
#define CMD_FIRE    0x10
#define CMD_STOP    0x20

class Launcher
{
private:
    usb_dev_handle *launcher;

public:
    void init();
    void deinit();
    void send(int cmd);
};