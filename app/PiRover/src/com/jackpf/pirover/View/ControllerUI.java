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
                
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX() - (ivSteeringWheel.getWidth() / 2);
                        float y = -(event.getY() - (ivSteeringWheel.getHeight() / 2));
                        
                        if (y < 0) {
                            y = 0;
                        }
                        
                        double angle = Math.toDegrees(Math.atan(y / x));
                        
                        if (x >= 0) {
                            angle = 90.0 - angle;
                        } else {
                            angle = -(90.0 + angle);
                        }
                        
                        Log.d("Touch", "x: " + x + ", y: " + y + ", degrees : " + angle);
                        
                        int position = (int) Math.round(angle / 9.0);
                        int roundedAngle = (int) (position * 9.0);
                        
                        Log.d("Touch", "rounded: " + roundedAngle  + ", position: " + position);
                        
                        Matrix matrix = new Matrix();
                        matrix.postRotate((float) roundedAngle, ivSteeringWheel.getWidth() / 2, ivSteeringWheel.getHeight() / 2);
                        ivSteeringWheel.setImageMatrix(matrix);
                        
                        controller.setSteeringPosition(position);
                    break;
                }
                
                return true;
            }
        });
        
        final ImageView ivAccelerator = (ImageView) ((Activity) context).findViewById(R.id.accelerator);
        
        ivAccelerator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        float height = ivAccelerator.getHeight() - ivAccelerator.getDrawable().getIntrinsicHeight();
                        float y = height - (event.getY() - (ivAccelerator.getDrawable().getIntrinsicHeight() / 2));
                        
                        if (y < 0) {
                            y = 0;
                        } else if (y > height) {
                            y = height;
                        }
                        
                        int position = (int) Math.round(y * 10.0 / height);
                        int roundedY = (int) (position * (height / 10.0));
                        
                        Log.d("Touch", "y: " + y + ", rounded: " + roundedY);
                        
                        Matrix matrix = new Matrix();
                        matrix.postTranslate(0, height - roundedY);
                        ivAccelerator.setImageMatrix(matrix);
                        
                        controller.setAcceleratorPosition(position);
                    break;
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