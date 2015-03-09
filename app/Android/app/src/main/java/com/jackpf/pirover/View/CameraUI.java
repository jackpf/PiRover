package com.jackpf.pirover.View;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.R;
import com.jackpf.pirover.Client.ClientException;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.View.EventListener.RecordButtonListener;

public class CameraUI extends UI<MainActivity>
{
    private Recorder recorder;
    private TextView tvStatus;
    private Button bRecording;
    private ImageView ivCamera;
    private TextView tvFpsCount, tvBandwidth;
    
    public CameraUI(MainActivity activity, Recorder recorder)
    {
        super(activity);

        this.recorder = recorder;
    }
    
    @Override
    public void initialise()
    {
        /**
         * Set views here so we don't have to repeatedly find them
         * in update() which is called pretty rapidly
         */
        tvStatus    = (TextView) activity.findViewById(R.id.camera_status);
        bRecording  = (Button) activity.findViewById(R.id.record);
        ivCamera    = (ImageView) activity.findViewById(R.id.camera);
        tvFpsCount  = (TextView) activity.findViewById(R.id.fps_counter);
        tvBandwidth = (TextView) activity.findViewById(R.id.bandwidth);

        ((Button) activity.findViewById(R.id.record)).setOnClickListener(new RecordButtonListener(recorder));
    }

    @Override
    public void preUpdate()
    {
        // Update state to connecting
        //if (!tvStatus.getText().equals(context.getString(R.string.camera_state_connected))) {
        //    tvStatus.setText(context.getString(R.string.camera_state_connecting));
        //}
        // Seems to make things flicker sometimes
    }

    @Override
    public void update()
    {
        // Update state to connected
        if (!tvStatus.getText().equals(context.getString(R.string.camera_state_connected))) {
            tvStatus.setText(context.getString(R.string.camera_state_connected));
        }
        
        // Update recording colour
        if ((Boolean) vars.get("recording")) {
            bRecording.setTextColor(Color.RED);
        } else {
            bRecording.setTextColor(Color.BLACK);
        }
        
        // Update image
        Drawable drawable = (Drawable) vars.get("drawable");
        ivCamera.setImageDrawable(drawable);

        // Fps counter & bandwidth
        displayStats((Double) vars.get("fpsCount"), (Double) vars.get("bandwidth"));
    }
    
    protected void displayStats(Double fpsCount, Double bandwidth)
    {
        // Fps counter
        if (fpsCount != null) {
            tvFpsCount.setText(
                String.format(
                    context.getString(R.string.camera_fps_count),
                    Math.round(fpsCount)
                )
            );
        }
        
        // Bandwidth
        if (bandwidth != null) {
            tvBandwidth.setText(
                String.format(
                    context.getString(R.string.camera_bandwidth),
                    Math.round(bandwidth / 1024)
                )
            );
        }
    }

    @Override
    public void error(Exception e)
    {
        // Update state to unable to connect
        if (e instanceof ClientException) {
            tvStatus.setText(context.getString(R.string.camera_state_unable_to_connect));
        }
        
        // Reset stats
        displayStats(0.0, 0.0);
    }
}