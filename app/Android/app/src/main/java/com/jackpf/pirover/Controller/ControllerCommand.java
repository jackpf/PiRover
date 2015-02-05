package com.jackpf.pirover.Controller;

public class ControllerCommand
{
    public final int id;
    public final byte[] bytes;

    public ControllerCommand(int id, byte[] bytes)
    {
        this.id = id;
        this.bytes = bytes;
    }
}