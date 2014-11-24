package com.jackpf.pirover.Controller;

public class ClientException extends com.jackpf.pirover.Client.ClientException
{
    private static final long serialVersionUID = 4339739609713664573L;

    public ClientException(String message)
    {
        super(message);
    }

    public ClientException(String message, Exception e)
    {
        super(message, e);
    }
}
