package com.jackpf.pirover.View;

import android.content.Context;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.View.EventListener.AcceleratorListener;
import com.jackpf.pirover.View.EventListener.SteeringWheelListener;

public class ControllerUI extends UI
{
    private Controller controller;
    
    public ControllerUI(Context context, Controller controller)
    {
        super(context);
        
        this.controller = controller;
    }
    
    @Override
    public void initialise()
    {
        // Set steering wheel touch listener
        ((ImageView) activity.findViewById(R.id.steering_wheel))
            .setOnTouchListener(new SteeringWheelListener(controller));
        
        // Set accelerator touch listener
        ((ImageView) activity.findViewById(R.id.accelerator))
            .setOnTouchListener(new AcceleratorListener(controller));
        
        /* Initially translate to the bottom
           ivAccelerator.getHeight() seems to return 0 at this point
           So 288 is hard coded (height - drawable height)
           TODO: Needs dynamically calculating (or gravity of the image
           view set to bottom, which doesn't seem possible with a matrix scale type */
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 288);
        ((ImageView) activity.findViewById(R.id.accelerator)).setImageMatrix(matrix);
    }
}