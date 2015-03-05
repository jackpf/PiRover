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
import com.jackpf.pirover.Controller.ControllerCommand;
import com.jackpf.pirover.Controller.Launcher;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Request.BroadcastRequest;
import com.jackpf.pirover.Request.CameraRequest;
import com.jackpf.pirover.Request.ControlRequest;
import com.jackpf.pirover.Request.SensorRequest;
import com.jackpf.pirover.Request.ShutdownRequest;
import com.jackpf.pirover.RequestThread.Callback;
import com.jackpf.pirover.Service.Utils;
import com.jackpf.pirover.View.BroadcastUI;
import com.jackpf.pirover.View.CameraUI;
import com.jackpf.pirover.View.ControllerUI;

import org.apache.commons.lang.ArrayUtils;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer
{
    /**
     * Network thread instances
     */
    protected RequestThread cameraThread, controlThread, sensorThread;
    
    /**
     * Client instances
     */
    protected Client cameraClient, controlClient;
    
    /**
     * Network request instances
     */
    protected Request cameraRequest, controlRequest, sensorRequest;
    
    /**
     * Controller instance
     */
    protected Controller controller;

    /**
     * Launcher instance
     */
    protected Launcher launcher;
    
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
        launcher        = new Launcher();
        launcher.addObserver(this);

        streamStats     = new StreamStats();
        recorder        = new Recorder(streamStats);
        
        cameraClient    = new com.jackpf.pirover.Camera.Client(new DrawableFrameFactory(), streamStats);
        controlClient   = new com.jackpf.pirover.Controller.Client();
        
        cameraRequest   = new CameraRequest(cameraClient, recorder);
        controlRequest  = new ControlRequest(controlClient, controller);
        sensorRequest   = new SensorRequest(controlClient);

        initialiseUI(new CameraUI(this, recorder), new ControllerUI(this, controller, launcher), new BroadcastUI(this));

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
            startThreads();
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
        
        stopThreads();
        
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
            case R.id.action_shutdown:
                new RequestThread(new ShutdownRequest(controlClient))
                    .setCallback(new Callback() {
                        @Override
                        public void onPostExecute(RequestResponse vars, Exception e) {
                            finish();
                        }
                    })
                    .execute(ip, Integer.parseInt(ports.get("control")));
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
                        
                        startThreads();
                    }
                }
            })
            .execute(Integer.parseInt(preferences.getString(getString(R.string.pref_broadcast_port_key), getString(R.string.pref_broadcast_port_default))), manualIp);
    }

    protected void startThreads()
    {System.err.println("start threads");
        // Start continuous threads
        executeCameraRequest();
        //executeSensorRequest();
    }

    protected void stopThreads()
    {
        if (cameraThread instanceof RequestThread) {
            cameraThread.cancel(true);
        }

        if (sensorThread instanceof RequestThread) {
            sensorThread.cancel(true);
        }
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
            cameraThread.execute(ip, Integer.parseInt(ports.get("camera")));
        }
    }

    // Continuously execute sensor requests
    protected void executeSensorRequest()
    {
        if (sensorThread instanceof RequestThread) {
            sensorThread.cancel(true);
        }

        final Handler handler = new Handler();

        sensorThread = new RequestThread(
            sensorRequest,
            getUI(ControllerUI.class)
        ).setCallback(new Callback() {
            @Override
            public void onPostExecute(RequestResponse vars, Exception e) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeSensorRequest();
                    }
                }, 1000);
            }
        });

        if (ip != null && ports != null) {
            sensorThread.execute(ip, Integer.parseInt(ports.get("control")));
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
                byte[] buf = ArrayUtils.addAll(
                    Utils.intToByteArray(controller.getAcceleration()),
                    Utils.intToByteArray(controller.getSteering())
                );

                controlThread.execute(ip, Integer.parseInt(ports.get("control")), new ControllerCommand(0x0, buf));
            }
        } else if (observer instanceof Launcher) {
            if (controlThread instanceof RequestThread) {
                controlThread.cancel(true);
            }

            controlThread = new RequestThread(controlRequest);

            if (ip != null && ports != null) {
                controlThread.execute(
                    ip,
                    Integer.parseInt(ports.get("control")),
                    new ControllerCommand(0x1, Utils.intToByteArray((Integer) data))
                );
            }
        }
    }
}
