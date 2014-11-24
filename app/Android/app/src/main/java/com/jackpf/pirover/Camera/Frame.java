package com.jackpf.pirover.Camera;

public abstract class Frame
{
    protected final byte[] image;
    
    public Frame(byte[] image)
    {
        this.image = image;
    }
    
    public byte[] getBytes()
    {
        return image;
    }
}
