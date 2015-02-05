package com.jackpf.pirover;

import android.app.Activity;
import android.os.AsyncTask;

import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;

/**
 * Network thread
 * Performs all api requests
 */
public class RequestThread extends AsyncTask<String, Void, Void>
{
    /**
     * Api request to call
     */
    private Request request;
    
    /**
     * UI to pass data to
     */
    private UI<? extends Activity> ui;
    
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
     * @param request
     */
    public RequestThread(Request request)
    {
        this.request = request;
    }
    
    /**
     * Constructor
     * 
     * @param request
     * @param ui
     */
    public RequestThread(Request request, UI<? extends Activity> ui)
    {
        this.request = request;
        this.ui = ui;
    }
    
    /**
     * Set callback
     * 
     * @param callback
     * @return
     */
    public RequestThread setCallback(Callback callback)
    {
        this.callback = callback;
        
        return this;
    }

    /**
     * Thread pre execute
     * Just call the UI's preExecute method
     */
    @Override
    protected void onPreExecute()
    {
        request.preCall();
        
        if (ui != null) {
            ui.preUpdate();
        }
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
            vars = request.call(params);
        } catch (Exception e) {
            // Always trace exceptions!
            e.printStackTrace();

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
        if (ui != null) {
            ui.setVars(vars);
    
            if (e == null) {
                ui.update();
            } else {
                ui.error(e);
            }
        }
        
        request.postCall(vars, e);
        
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