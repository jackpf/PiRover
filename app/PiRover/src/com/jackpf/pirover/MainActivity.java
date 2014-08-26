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
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        ui = new MainActivityUI(this);
        
        //refresh();
    }
    
    public void onClick(View v)
    {
        refresh();
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
    protected void refresh()
    {
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
        
        thread = new NetworkThread(
            this,
            new CameraRequest(),
            ui
        );
        
        thread.setCallback(new Callback() {
            public void onPostExecute() {
                refresh();
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
