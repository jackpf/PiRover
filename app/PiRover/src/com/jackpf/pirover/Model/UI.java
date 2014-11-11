package com.jackpf.pirover.Model;

import android.app.Activity;
import android.content.Context;

/**
 * UI interface
 */
public abstract class UI
{
    /**
     * Parent context
     */
    protected Context context;
    
    /**
     * Parent activity
     */
    protected Activity activity;
    
    /**
     * UI vars
     */
    protected RequestResponse vars;
    
    /**
     * Constructor
     * 
     * @param context
     */
    public UI(Context context)
    {
        this.context = context;
        this.activity = (Activity) context;
    }
    
    /**
     * Set vars
     * 
     * @param vars
     */
    public void setVars(RequestResponse vars)
    {
        this.vars = vars;
    }
    
    /**
     * Initialise UI
     * Should be called in activity onCreate
     */
    public void initialise()
    {
        
    }
    
    /**
     * Called pre update from thread
     */
    public void preUpdate()
    {
        
    }
    
    /**
     * Update ui
     */
    public void update()
    {
        
    }
    
    /**
     * Render error
     * 
     * @param e
     */
    public void error(Exception e)
    {
        
    }
}
