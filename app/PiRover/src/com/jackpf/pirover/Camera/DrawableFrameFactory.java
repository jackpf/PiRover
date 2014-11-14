package com.jackpf.pirover.Camera;

public class DrawableFrameFactory implements FrameFactory
{
    public Frame createFrame(byte[] image)
    {
        return new DrawableFrame(image);
    }
}
