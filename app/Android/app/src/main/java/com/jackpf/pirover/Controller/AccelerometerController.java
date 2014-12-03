package com.jackpf.pirover.Controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class AccelerometerController implements SensorEventListener
{
    /**
     * Gravity vector
     */
    private float gravity[] = new float[3];

    /**
     * Geomagnetic vector
     */
    private float geomagnetic[] = new float[3];

    /**
     * Gravity vector initialised
     */
    private boolean gravityInitialised = false;

    /**
     * Geomagnetic vector initialised
     */
    private boolean geomagneticInitialised = false;

    /**
     * Pitch range
     * Range of rotation around the z axis in degrees for steering
     */
    private final int pitchRange[] = {-70, -30};

    /**
     * Roll range
     * Range of rotation around the x axis in degrees for acceleration
     */
    final int rollRange[] = {-45, 45};

    /**
     * Controller instance
     */
    private Controller controller;

    /**
     * Constructor
     *
     * @param controller
     */
    public AccelerometerController(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, gravity, 0, 3);
                gravityInitialised = true;
            break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, geomagnetic, 0, 3);
                geomagneticInitialised = true;
            break;
        }

        if (gravityInitialised && geomagneticInitialised) {
            float r[] = new float[9], v[] = new float[3];

            if (SensorManager.getRotationMatrix(r, null, gravity, geomagnetic)) {
                SensorManager.getOrientation(r, v);

                int pitch = (int) Math.round(Math.toDegrees(v[1])),
                    roll = (int) Math.round(Math.toDegrees(v[2]));

                if (pitch < pitchRange[0]) {
                    pitch = pitchRange[0];
                } else if (pitch > pitchRange[1]) {
                    pitch = pitchRange[1];
                }
                pitch = (int) Math.round((double) (pitch + Math.abs(pitchRange[0])) / (pitchRange[1] - pitchRange[0]) * 10.0);

                if (roll < rollRange[0]) {
                    roll = rollRange[0];
                } else if (roll > rollRange[1]) {
                    roll = rollRange[1];
                }
                roll = (int) Math.round((double) (roll + Math.abs(rollRange[0])) / (rollRange[1] - rollRange[0]) * 20.0) - 10 /* double the range but keeping the accuracy */;

                controller.setAcceleration(pitch);
                controller.setSteering(roll);
            } else {
                Log.e(getClass().getName(), "SensorManager.getRotationMatrix failed");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
        Log.d(getClass().getName(), "Accuracy changed");
    }
}
