package com.jackpf.pirover.Broadcast;

public class TimeoutException extends Exception
{
    private static final long serialVersionUID = -7552343606958596724L;

    public TimeoutException(String message)
    {
        super(message);
    }

    public TimeoutException(String message, Exception e)
    {
        super(message, e);
    }
}
