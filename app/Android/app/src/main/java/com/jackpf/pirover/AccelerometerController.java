package com.jackpf.pirover;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.jackpf.pirover.Controller.Controller;

public class AccelerometerController implements SensorEventListener
{
    float gravity[] = new float[3], geomagnetic[] = new float[3];
    boolean gravityObtained = false, geomagneticObtained = false;

    final int pitchRange[] = {-70, -30}, rollRange[] = {-45, 45};

    private Controller controller;

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
                gravityObtained = true;
            break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, geomagnetic, 0, 3);
                geomagneticObtained = true;
            break;
        }

        if (gravityObtained && geomagneticObtained) {
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
                roll = (int) Math.round((double) (roll + Math.abs(rollRange[0])) / (rollRange[1] - rollRange[0]) * 20.0) - 10;

                controller.setAcceleratorPosition(pitch);
                controller.setSteeringPosition(roll);
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
