package com.jackpf.pirover.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Controller.ControllerCalculator;
import com.jackpf.pirover.Model.UI;

public class ControllerUI extends UI
{
    private Controller controller;
    
    public ControllerUI(Context context, Controller controller)
    {
        super(context);
        
        this.controller = controller;
    }
    
    public void initialise()
    {
        final ImageView ivSteeringWheel = (ImageView) ((Activity) context).findViewById(R.id.steering_wheel);
        
        ivSteeringWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    ControllerCalculator.Position steeringWheelPosition
                        = ControllerCalculator.calculateSteeringPosition(event.getX(), event.getY(), ivSteeringWheel.getWidth(), ivSteeringWheel.getHeight());
                    
                    Log.d("Touch", "position: " + steeringWheelPosition.position  + ", value: " + steeringWheelPosition.value);
                    
                    Matrix matrix = new Matrix();
                    matrix.postRotate((float) steeringWheelPosition.position, ivSteeringWheel.getWidth() / 2, ivSteeringWheel.getHeight() / 2);
                    ivSteeringWheel.setImageMatrix(matrix);
                    
                    controller.setSteeringPosition(steeringWheelPosition.value);
                }
                
                return true;
            }
        });
        
        final ImageView ivAccelerator = (ImageView) ((Activity) context).findViewById(R.id.accelerator);
        
        ivAccelerator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    ControllerCalculator.Position acceleratorPosition
                        = ControllerCalculator.calculateAcceleratorPosition(event.getY(), ivAccelerator.getWidth(), ivAccelerator.getHeight(), ivAccelerator.getDrawable().getIntrinsicHeight());
                
                    Log.d("Touch", "position: " + acceleratorPosition.position + "value: " + acceleratorPosition.value);
                    
                    Matrix matrix = new Matrix();
                    matrix.postTranslate(0, acceleratorPosition.position);
                    ivAccelerator.setImageMatrix(matrix);
                    
                    controller.setAcceleratorPosition(acceleratorPosition.value);
                }
                
                return true;
            }
        });
    }
    
    public void preUpdate()
    {
        
    }
    
    public void update()
    {
        
    }
    
    public void error(Exception e)
    {
        e.printStackTrace();
    }
}