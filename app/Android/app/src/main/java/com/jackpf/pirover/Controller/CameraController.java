package com.jackpf.pirover.Controller;

import java.util.Observable;

public class CameraController extends Observable
{
    public void setMotionDetection(boolean motionDetection)
    {
        setChanged();
        notifyObservers(motionDetection);
    }
}
