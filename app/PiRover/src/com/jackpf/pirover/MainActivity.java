package com.jackpf.pirover;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jackpf.pirover.NetworkThread.Callback;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.Request.BroadcastRequest;
import com.jackpf.pirover.Request.CameraRequest;
import com.jackpf.pirover.View.BroadcastUI;
import com.jackpf.pirover.View.CameraUI;
import com.jackpf.pirover.View.ControllerUI;

public class MainActivity extends ActionBarActivity
{
    /**
     * Network thread instance
     */
    protected NetworkThread thread;
    
    /**
     * Network request instances
     */
    protected Request cameraRequest, controlRequest;
    
    /**
     * User interface instances
     */
    protected UI cameraUI, controlUI;
    
    /**
     * Controller instance
     */
    protected Controller controller;
    
    /**
     * Resolved IP address
     */
    protected String ip;
    
    /**
     * Activity created event
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        cameraRequest = new CameraRequest();
        controller = new Controller();

        cameraUI = new CameraUI(this);
        controlUI = new ControllerUI(this, controller);

        cameraUI.initialise();
        controlUI.initialise();
    }
    
    /**
     * Continuously executes camera requests
     */
    protected void executeCameraRequest()
    {
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
        
        thread = new NetworkThread(
            cameraRequest,
            cameraUI
        );
        
        // Set repeating
        thread.setCallback(new Callback() {
            public void onPostExecute(RequestResponse vars, Exception e) {
                int delay = !(e instanceof ClientException) ? 0 : 5000;
                
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeCameraRequest();
                    }
                }, delay);
            }
        });
        
        thread.execute(ip);
    }
    
    /**
     * Activity resumed event
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        if (ip == null) {
            new NetworkThread(new BroadcastRequest((WifiManager) getSystemService(WIFI_SERVICE)), new BroadcastUI(this))
                .setCallback(new NetworkThread.Callback() {
                    @Override
                    public void onPostExecute(RequestResponse vars, Exception e) {
                        ip = (String) vars.get("ip");
                        Log.d("Broadcast", "Resolved IP: " + ip);
                    }
                })
                .execute();
        }
        
        executeCameraRequest();
    }
    
    /**
     * Activity paused event
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
    }

    /**
     * Menu create event
     * 
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }

    /**
     * Menu item click event
     * 
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Record button click event
     * 
     * @param v
     */
    public void onRecordClick(View v)
    {
        ((CameraRequest) cameraRequest).getRecorder().toggleRecording();
    }
    
    /**
     * Playback button click event
     * 
     * @param v
     */
    public void onPlaybackClick(View v)
    {
        Intent intent = new Intent(this, PlaybackActivity.class);
        startActivity(intent);
    }
}
