package com.jackpf.pirover.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackpf.pirover.Controller.CameraController;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Controller.Launcher;
import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.R;
import com.jackpf.pirover.View.EventListener.AcceleratorListener;
import com.jackpf.pirover.View.EventListener.LauncherListener;
import com.jackpf.pirover.View.EventListener.MotionListener;
import com.jackpf.pirover.View.EventListener.SteeringWheelListener;

public class ControllerUI extends UI<MainActivity>
{
    private Controller controller;
    private Launcher launcher;
    private CameraController cameraController;

    public ControllerUI(MainActivity activity, Controller controller, Launcher launcher, CameraController cameraController)
    {
        super(activity);

        this.controller = controller;
        this.launcher = launcher;
        this.cameraController = cameraController;
    }
    
    @Override
    public void initialise()
    {
        // Set steering wheel touch listener and controller observer
        ImageView ivSteeringWheel = (ImageView) activity.findViewById(R.id.steering_wheel);
        ivSteeringWheel.setOnTouchListener(new SteeringWheelListener(controller, ivSteeringWheel));
        controller.addObserver(new SteeringWheelListener(controller, ivSteeringWheel));
        
        // Set accelerator touch listener
        ImageView ivAccelerator = (ImageView) activity.findViewById(R.id.accelerator);
        ivAccelerator.setOnTouchListener(new AcceleratorListener(controller, ivAccelerator));
        controller.addObserver(new AcceleratorListener(controller, ivAccelerator));

        // Launcher touch listeners
        LauncherListener launcherListener = new LauncherListener(launcher);
        ((Button) activity.findViewById(R.id.launcher_left)).setOnTouchListener(launcherListener);
        ((Button) activity.findViewById(R.id.launcher_right)).setOnTouchListener(launcherListener);
        ((Button) activity.findViewById(R.id.launcher_up)).setOnTouchListener(launcherListener);
        ((Button) activity.findViewById(R.id.launcher_down)).setOnTouchListener(launcherListener);
        ((ImageButton) activity.findViewById(R.id.launcher_fire)).setOnTouchListener(launcherListener);

        ((ImageButton) activity.findViewById(R.id.camera_motion)).setOnTouchListener(new MotionListener(cameraController));
    }

    @Override
    public void update()
    {
        if (vars.get("distance") != null) {
            ((TextView) activity.findViewById(R.id.sensor)).setText(
                String.format(activity.getString(R.string.sensor), (Integer) vars.get("distance"))
            );
        }
    }
}