package com.jackpf.pirover.Camera;

public class ClientException extends Exception
{
    private static final long serialVersionUID = 4535506108817453998L;

    public ClientException(String message)
    {
        super(message);
    }

    public ClientException(String message, Exception e)
    {
        super(message, e);
    }
}
