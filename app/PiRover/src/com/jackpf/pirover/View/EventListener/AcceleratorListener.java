package com.jackpf.pirover.View.EventListener;

import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Controller.ControllerCalculator;

public class AcceleratorListener implements View.OnTouchListener
{
    private Controller controller;
    
    public AcceleratorListener(Controller controller)
    {
        this.controller = controller;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();
        
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ImageView iv = (ImageView) v;
            
            ControllerCalculator.Position acceleratorPosition
                = ControllerCalculator.calculateAcceleratorPosition(event.getY(), iv.getWidth(), iv.getHeight(), iv.getDrawable().getIntrinsicHeight());
        
            Log.d("Touch", "position: " + acceleratorPosition.position + ", value: " + acceleratorPosition.value);
            
            Matrix matrix = new Matrix();
            matrix.postTranslate(0, acceleratorPosition.position);
            iv.setImageMatrix(matrix);
            
            controller.setAcceleratorPosition(acceleratorPosition.value);
            
            return true;
        }
        
        return false;
    }
}
