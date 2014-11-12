package com.jackpf.pirover.View;

import android.graphics.Matrix;
import android.widget.ImageView;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.R;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.View.EventListener.AcceleratorListener;
import com.jackpf.pirover.View.EventListener.SteeringWheelListener;

public class ControllerUI extends UI<MainActivity>
{
    public ControllerUI(MainActivity activity)
    {
        super(activity);
    }
    
    @Override
    public void initialise()
    {
        // Set steering wheel touch listener
        ((ImageView) activity.findViewById(R.id.steering_wheel))
            .setOnTouchListener(new SteeringWheelListener(activity));
        
        // Set accelerator touch listener
        ((ImageView) activity.findViewById(R.id.accelerator))
            .setOnTouchListener(new AcceleratorListener(activity));
        
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