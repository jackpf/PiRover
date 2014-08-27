package com.jackpf.pirover.Camera;

public class ClientException extends Exception
{
    public ClientException(String message)
    {
        super(message);
    }

    public ClientException(String message, Exception e)
    {
        super(message, e);
    }
}
