package com.jackpf.pirover.View.EventListener;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Controller.ControllerCalculator;

import java.util.Observable;
import java.util.Observer;

public class AcceleratorListener implements View.OnTouchListener, Observer
{
    private Controller controller;
    private ImageView iv;

    public AcceleratorListener(Controller controller, ImageView iv)
    {
        this.controller = controller;
        this.iv = iv;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();
        
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ImageView iv = (ImageView) v;
            
            controller.setAcceleration(
                ControllerCalculator
                    .calculateAcceleratorValue(event.getY(), iv.getWidth(), iv.getHeight(), iv.getDrawable().getIntrinsicHeight())
            );
        }
        
        return true;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, ControllerCalculator.calculateAcceleratorPosition(
            controller.getAcceleration(),
            iv.getHeight(),
            iv.getDrawable().getIntrinsicHeight()
        ));
        iv.setImageMatrix(matrix);
    }
}
