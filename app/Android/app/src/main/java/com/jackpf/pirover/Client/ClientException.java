package com.jackpf.pirover.Client;

public class ClientException extends Exception
{
    private static final long serialVersionUID = -444013374669284262L;

    public ClientException(String message)
    {
        super(message);
    }

    public ClientException(String message, Exception e)
    {
        super(message, e);
    }
}
