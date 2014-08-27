package com.jackpf.pirover;

import android.content.Context;
import android.os.AsyncTask;

import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;

/**
 * Network thread
 * Performs all api requests
 */
public class NetworkThread extends AsyncTask<String, Void, Void>
{
    /**
     * Context called from
     */
    // Not really needed
    //private Context context;
    
    /**
     * Api request to call
     */
    private Request request;
    
    /**
     * UI to pass data to
     */
    private UI ui;
    
    /**
     * UI vars
     */
    RequestResponse vars = new RequestResponse();
    
    /**
     * Exception caused in the network thread
     */
    Exception e = null;
    
    /**
     * Callback
     */
    Callback callback = null;
    
    /**
     * Constructor
     * 
     * @param context
     * @param request
     * @param ui
     */
    public NetworkThread(Context context, Request request, UI ui)
    {
        //this.context = context;
        this.request = request;
        this.ui = ui;
    }
    
    /**
     * Set callback
     * 
     * @param callback
     */
    public void setCallback(Callback callback)
    {
        this.callback = callback;
    }

    /**
     * Thread pre execute
     * Just call the UI's preExecute method
     */
    @Override
    protected void onPreExecute()
    {
        ui.preUpdate();
    }

    /**
     * Perform request
     * 
     * @param params
     */
    @Override
    protected Void doInBackground(String... params)
    {
        try {
            // Just set the vars to the response
            vars = request.call();
        } catch (Exception e) {
            this.e = e;
        }

        return null;
    }
    
    /**
     * Post execute
     * Pass vars to UI
     * 
     * @param _void
     */
    @Override
    protected void onPostExecute(Void _void)
    {
        ui.setVars(vars);

        if (e == null) {
            ui.update();
        } else {
            ui.error(e);
        }
        
        if (callback != null) {
            callback.onPostExecute(vars, e);
        }
    }
    
    /**
     * Callback interface
     */
    public interface Callback
    {
        public void onPostExecute(RequestResponse vars, Exception e);
    }
}