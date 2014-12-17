package com.jackpf.pirover;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jackpf.pirover.Broadcast.BroadcastResolver;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.DrawableFrameFactory;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Camera.StreamStats;
import com.jackpf.pirover.Client.Client;
import com.jackpf.pirover.Controller.AccelerometerController;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Request.BroadcastRequest;
import com.jackpf.pirover.Request.CameraRequest;
import com.jackpf.pirover.Request.ControlRequest;
import com.jackpf.pirover.RequestThread.Callback;
import com.jackpf.pirover.View.BroadcastUI;
import com.jackpf.pirover.View.CameraUI;
import com.jackpf.pirover.View.ControllerUI;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer
{
    /**
     * Network thread instances
     */
    protected RequestThread cameraThread, controlThread;
    
    /**
     * Client instances
     */
    protected Client cameraClient, controlClient;
    
    /**
     * Network request instances
     */
    protected Request cameraRequest, controlRequest;
    
    /**
     * Controller instance
     */
    protected Controller controller;
    
    /**
     * Recorder instance
     */
    protected Recorder recorder;
    
    /**
     * Stream stats instance
     */
    protected StreamStats streamStats;
    
    /**
     * Resolved IP address
     */
    protected static String ip;
    
    /**
     * Resolved ports
     */
    static BroadcastResolver.PortMap ports;

    /**
     * Accelerometer controller instance
     * Not set if turned off in preferences
     */
    private AccelerometerController accelerometerController;
    
    /**
     * Activity created event
     * Initialise clients, requests and UIs
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState, false);
        
        setContentView(R.layout.activity_main);

        controller      = new Controller();
        controller.addObserver(this);

        streamStats     = new StreamStats();
        recorder        = new Recorder(streamStats);
        
        cameraClient    = new com.jackpf.pirover.Camera.Client(new DrawableFrameFactory(), streamStats);
        controlClient   = new com.jackpf.pirover.Controller.Client();
        
        cameraRequest   = new CameraRequest(cameraClient, recorder);
        controlRequest  = new ControlRequest(controlClient, controller);

        initialiseUI(new CameraUI(this, recorder), new ControllerUI(this, controller), new BroadcastUI(this));

        if (preferences.getBoolean(getString(R.string.pref_gyroscope_key), Boolean.valueOf(getString(R.string.pref_gyroscope_default)))) {
            accelerometerController =
                new AccelerometerController(controller, preferences.getBoolean(
                    getString(R.string.pref_gyroscope_smoothing_key),
                    Boolean.valueOf(getString(R.string.pref_gyroscope_smoothing_default))
                ));
        }
    }
    
    /**
     * Activity resumed event
     * Attempt to (re)connect to the PiRover
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        if (ip == null || ports == null) {
            if (preferences.getBoolean(
                getString(R.string.pref_autoconnect_key),
                Boolean.valueOf(getString(R.string.pref_autoconnect_default))
            )) {
                connect(null);
            }
        } else {
            executeCameraRequest();
        }

        if (accelerometerController != null) {
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            sensorManager.registerListener(
                accelerometerController,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            );

            sensorManager.registerListener(
                accelerometerController,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }
    
    /**
     * Activity paused event
     * Cancel any running threads and disconnect clients
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        if (cameraThread instanceof RequestThread) {
            cameraThread.cancel(true);
        }
        
        cameraClient.disconnect();
        controlClient.disconnect();

        if (accelerometerController != null) {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE))
                .unregisterListener(accelerometerController);
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
        
        switch (id) {
            case R.id.action_playback:
                startActivity(BrowseActivity.class);
                return true;
            case R.id.action_connect:
                connect(null);
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Resolve IP and ports
     * 
     * @param manualIp
     */
    public void connect(String manualIp)
    {
        new RequestThread(new BroadcastRequest(getSystemService(WIFI_SERVICE), getSystemService(Context.CONNECTIVITY_SERVICE)), getUI(BroadcastUI.class))
            .setCallback(new RequestThread.Callback() {
                @Override
                public void onPostExecute(RequestResponse vars, Exception e) {
                    if (vars.get("ip") != null && !(e instanceof Exception)) {
                        ip = (String) vars.get("ip");
                        Log.d("Broadcast", "Resolved IP: " + ip);
                        
                        ports = (BroadcastResolver.PortMap) vars.get("ports");
                        Log.d("Broadcast", "Controller port: " + ports.get("control") + ", camera port: " + ports.get("camera"));
                        
                        // Start connecting to camera
                        executeCameraRequest();
                    }
                }
            })
            .execute(preferences.getString(getString(R.string.pref_broadcast_port_key), getString(R.string.pref_broadcast_port_default)), manualIp);
    }
    
    /**
     * Continuously executes camera requests
     */
    protected void executeCameraRequest()
    {
        if (cameraThread instanceof RequestThread) {
            cameraThread.cancel(true);
        }

        final Handler handler = new Handler();
        
        cameraThread = new RequestThread(
            cameraRequest,
            getUI(CameraUI.class)
        ).setCallback(new Callback() {
            @Override
            public void onPostExecute(RequestResponse vars, Exception e) {
                int delay = !(e instanceof ClientException) ? 0 : 5000;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeCameraRequest();
                    }
                }, delay);
            }
        });

        if (ip != null && ports != null) {
            cameraThread.execute(ip, ports.get("camera"));
        }
    }
    
    /**
     * Execute control request listener
     */
    @Override
    public void update(Observable observer, Object data)
    {
        if (observer instanceof Controller) {
            if (controlThread instanceof RequestThread) {
                controlThread.cancel(true);
            }

            controlThread = new RequestThread(controlRequest);

            if (ip != null && ports != null) {
                controlThread.execute(ip, ports.get("control"));
            }
        }
    }
}
