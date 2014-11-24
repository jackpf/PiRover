package com.jackpf.pirover.View.EventListener;

import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Controller.ControllerCalculator;

public class SteeringWheelListener implements View.OnTouchListener
{
    private MainActivity activity;
    
    public SteeringWheelListener(MainActivity activity)
    {
        this.activity = activity;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();
        
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ImageView iv = (ImageView) v;
            
            ControllerCalculator.Position steeringWheelPosition
                = ControllerCalculator.calculateSteeringPosition(event.getX(), event.getY(), iv.getWidth(), iv.getHeight());
            
            Log.d("Touch", "position: " + steeringWheelPosition.position  + ", value: " + steeringWheelPosition.value);
            
            Matrix matrix = new Matrix();
            matrix.postRotate((float) steeringWheelPosition.position, iv.getWidth() / 2, iv.getHeight() / 2);
            iv.setImageMatrix(matrix);
            
            activity.setSteeringWheelPosition(steeringWheelPosition);
        }
        
        return true;
    }
}