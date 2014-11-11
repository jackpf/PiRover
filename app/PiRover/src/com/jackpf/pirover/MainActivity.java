package com.jackpf.pirover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jackpf.pirover.NetworkThread.Callback;
import com.jackpf.pirover.Broadcast.BroadcastResolver;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Client.Client;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.Request.BroadcastRequest;
import com.jackpf.pirover.Request.CameraRequest;
import com.jackpf.pirover.View.BroadcastUI;
import com.jackpf.pirover.View.CameraUI;
import com.jackpf.pirover.View.ControllerUI;

public class MainActivity extends Activity
{
    /**
     * Network thread instance
     */
    protected NetworkThread thread;
    
    /**
     * Client instances
     */
    protected Client cameraClient, controlClient;
    
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
    protected static String ip;
    
    /**
     * Resolved ports
     */
    BroadcastResolver.PortMap ports;
    
    /**
     * Activity created event
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        cameraClient = new com.jackpf.pirover.Camera.Client();
        controlClient = new com.jackpf.pirover.Controller.Client();
        
        cameraRequest = new CameraRequest(cameraClient);
        controller = new Controller(controlClient);

        cameraUI = new CameraUI(this);
        controlUI = new ControllerUI(this, controller);

        cameraUI.initialise();
        controlUI.initialise();
    }
    
    /**
     * Activity resumed event
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        if (ip == null) {
            connect(null);
        } else {
            executeCameraRequest();
        }
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
        
        cameraClient.disconnect();
        controlClient.disconnect();
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
        
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_playback:
                startPlaybackActivity();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Toggle recording
     */
    public void toggleRecording()
    {
        ((CameraRequest) cameraRequest).getRecorder().toggleRecording();
    }
    
    /**
     * Launch playback activity
     */
    public void startPlaybackActivity()
    {
        Intent intent = new Intent(this, PlaybackActivity.class);
        startActivity(intent);
    }
    
    /**
     * Resolve IP and ports
     * 
     * @param manualIp
     */
    public void connect(String manualIp)
    {
        new NetworkThread(new BroadcastRequest(getSystemService(WIFI_SERVICE), getSystemService(Context.CONNECTIVITY_SERVICE)), new BroadcastUI(this))
            .setCallback(new NetworkThread.Callback() {
                @Override
                public void onPostExecute(RequestResponse vars, Exception e) {
                    if (vars.get("ip") != null && !(e instanceof Exception)) {
                        ip = (String) vars.get("ip");
                        Log.d("Broadcast", "Resolved IP: " + ip);
                        
                        ports = (BroadcastResolver.PortMap) vars.get("ports");
                        Log.d("Broadcast", "Controller port: " + ports.get("control") + ", camera port: " + ports.get("camera"));
                        
                        // Start connecting to camera
                        executeCameraRequest();
                        
                        // Set controller IP and port
                        controller.setIp(ip);
                        controller.setPort(ports.get("control"));
                    }
                }
            })
            .execute(manualIp);
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
            @Override
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
        
        thread.execute(ip, ports.get("camera"));
    }
}
