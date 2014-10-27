package com.jackpf.pirover.Broadcast;

public class ConnectionException extends Exception
{
    private static final long serialVersionUID = -500174716164129391L;

    public ConnectionException(String message)
    {
        super(message);
    }

    public ConnectionException(String message, Exception e)
    {
        super(message, e);
    }
}
