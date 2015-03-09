package com.jackpf.pirover.View.EventListener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.jackpf.pirover.Controller.CameraController;
import com.jackpf.pirover.R;

public class MotionListener implements View.OnTouchListener
{
    private CameraController cameraController;
    private boolean motionDetection = false;

    public MotionListener(CameraController cameraController)
    {
        this.cameraController = cameraController;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            motionDetection = !motionDetection;

            if (motionDetection) {
                ((ImageButton) v).setImageResource(R.drawable.motion_on);
            } else {
                ((ImageButton) v).setImageResource(R.drawable.motion_off);
            }

            cameraController.setMotionDetection(motionDetection);
        }
        
        return true;
    }
}
