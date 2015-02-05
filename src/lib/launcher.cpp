#include "launcher.hpp"

void Launcher::init()
{
    usb_init();
    usb_find_busses();
    usb_find_devices();

    struct usb_bus *busses = usb_get_busses();
    struct usb_bus *bus;

    for (bus = busses; bus; bus = bus->next) {
        struct usb_device *dev;

        for (dev = bus->devices; dev; dev = dev->next) {
            if (dev->descriptor.idVendor == LAUNCHER_DEVICE_ID) {
                Lib::println("Launcher found!");

                launcher = usb_open(dev);

                char driverName[32];
                if (usb_get_driver_np(launcher, 0, driverName, 31) == 0) {
                    Lib::println("Claimed by %s", driverName);

                    if (usb_detach_kernel_driver_np(launcher, 0) == 0) {
                        Lib::println("Detached %s", driverName);
                    } else {
                        Lib::println("%s can't be detached", driverName);
                        return;
                    }
                }

                if (usb_claim_interface(launcher, 0) == 0) {
                    Lib::println("Device claimed");
                } else {
                    perror("Unable to claim device");
                }
            }
        }
    }
}

void Launcher::deinit()
{
    usb_release_interface(launcher, 0);
    usb_close(launcher);
}

void Launcher::send(int cmd)
{
    char msg[8] = {0x0};
    msg[0] = 0x02;
    msg[1] = cmd;

    if (usb_control_msg(launcher, 0x21, 0x9, 0, 0, msg, 8, 1000) < 0) {
        perror("Cannot send command");
    }
}