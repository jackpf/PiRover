package com.jackpf.pirover.Model;


/**
 * Request interface
 */
public abstract class Request
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
    public Request(Object ...params)
    {
        this.params = params;
    }
    
    /**
     * Perform api call
     * 
     * @return
     * @throws Exception
     */
    public abstract RequestResponse call(String ...args) throws Exception;
    
    /**
     * Called before request
     */
    public void preCall()
    {
        
    }
    
    /**
     * Called after request
     */
    public void postCall(RequestResponse vars, Exception e)
    {
        
    }
}