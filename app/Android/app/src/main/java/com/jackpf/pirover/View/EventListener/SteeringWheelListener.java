package com.jackpf.pirover.View.EventListener;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Controller.ControllerCalculator;

import java.util.Observable;
import java.util.Observer;

public class SteeringWheelListener implements View.OnTouchListener, Observer
{
    private Controller controller;
    private ImageView iv;
    
    public SteeringWheelListener(Controller controller, ImageView iv)
    {
        this.controller = controller;
        this.iv = iv;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();
        
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            controller.setSteering(
                ControllerCalculator
                    .calculateSteeringValue(event.getX(), event.getY(), iv.getWidth(), iv.getHeight())
            );
        }

        return true;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(
            ControllerCalculator.calculateSteeringPosition(controller.getSteering()),
            iv.getWidth() / 2,
            iv.getHeight() / 2
        );
        iv.setImageMatrix(matrix);
    }
}