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
import com.jackpf.pirover.Control.Controller;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.Request.BroadcastRequest;
import com.jackpf.pirover.Request.CameraRequest;
import com.jackpf.pirover.View.BroadcastUI;
import com.jackpf.pirover.View.CameraUI;
import com.jackpf.pirover.View.ControlUI;

public class MainActivity extends ActionBarActivity
{
    protected NetworkThread thread;
    protected UI cameraUI, controlUI;
    protected /*Request*/CameraRequest cameraRequest;
    protected Request controlRequest;
    protected Controller controller;
    protected static String ip;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        cameraRequest = new CameraRequest();
        controller = new Controller();

        cameraUI = new CameraUI(this);
        controlUI = new ControlUI(this, controller);

        cameraUI.initialise();
        controlUI.initialise();
    }
    
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
    
    @Override
    protected void onPause()
    {
        super.onPause();
        
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
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
    
    public void onRecordClick(View v)
    {
        cameraRequest.getRecorder().toggleRecording();
    }
    
    public void playback(View v)
    {
        Intent intent = new Intent(this, PlaybackActivity.class);
        startActivity(intent);
    }
    
    /**
     * Runs the network thread and updates the UI
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
}
