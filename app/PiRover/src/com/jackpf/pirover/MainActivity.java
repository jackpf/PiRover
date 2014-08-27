package com.jackpf.pirover;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jackpf.pirover.NetworkThread.Callback;
import com.jackpf.pirover.Model.UIInterface;
import com.jackpf.pirover.Request.CameraRequest;
import com.jackpf.pirover.View.MainActivityUI;

public class MainActivity extends ActionBarActivity
{
    protected NetworkThread thread;
    protected UIInterface ui;
    protected /*RequestInterface*/CameraRequest request;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        ui = new MainActivityUI(this);
        request = new CameraRequest();
    }
    
    public void onConnectClick(View v)
    {
        run();
    }
    
    public void onRecordClick(View v)
    {Log.d("d", "click");
        request.getRecorder().toggleRecording();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        
        thread.cancel(true);
    }
    
    /**
     * Runs the network thread and updates the UI
     * Sectioned off since it's called from onCreate and from the refresh button
     */
    protected void run()
    {
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
        
        thread = new NetworkThread(
            this,
            request,
            ui
        );
        
        thread.setCallback(new Callback() {
            public void onPostExecute() {
                run();
            }
        });
        
        thread.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
