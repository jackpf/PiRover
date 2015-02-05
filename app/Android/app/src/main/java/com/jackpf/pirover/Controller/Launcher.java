package com.jackpf.pirover.Controller;

import java.util.Observable;

public class Launcher extends Observable
{
    public static final int LAUNCHER_DEVICE_ID = 0x2123;
    public static final int LAUNCHER_DOWN      = 0x01;
    public static final int LAUNCHER_UP        = 0x02;
    public static final int LAUNCHER_LEFT      = 0x04;
    public static final int LAUNCHER_RIGHT     = 0x08;
    public static final int LAUNCHER_FIRE      = 0x10;
    public static final int LAUNCHER_STOP      = 0x20;

    public void left()
    {
        setChanged();
        notifyObservers(LAUNCHER_LEFT);
    }

    public void right()
    {
        setChanged();
        notifyObservers(LAUNCHER_RIGHT);
    }

    public void up()
    {
        setChanged();
        notifyObservers(LAUNCHER_UP);
    }

    public void down()
    {
        setChanged();
        notifyObservers(LAUNCHER_DOWN);
    }

    public void stop()
    {
        setChanged();
        notifyObservers(LAUNCHER_STOP);
    }

    public void fire()
    {
        setChanged();
        notifyObservers(LAUNCHER_FIRE);
    }
}
