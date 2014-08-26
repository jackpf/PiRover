package com.jackpf.pirover.Model;

import java.io.IOException;

import org.apache.http.ParseException;

import com.jackpf.pirover.Entity.RequestResponse;

/**
 * Request interface
 */
public abstract class RequestInterface
{
    /**
     * Api call params
     */
    protected Object[] params;
    
    /**
     * Constructor
     * 
     * @param params
     */
    public RequestInterface(Object ...params)
    {
        this.params = params;
    }
    
    /**
     * Perform api call
     * 
     * @return
     * @throws Exception
     */
    public abstract RequestResponse call() throws ParseException, IOException;
}