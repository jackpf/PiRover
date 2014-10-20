package com.jackpf.pirover.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Model.UI;

public class CameraUI extends UI
{
    private TextView tvStatus;
    
    public CameraUI(Context context)
    {
        super(context);
    }
    
    public void initialise()
    {
        tvStatus = (TextView) activity.findViewById(R.id.camera_status);
    }
    
    public void preUpdate()
    {
        // Update state to connecting
        if (!tvStatus.getText().equals(context.getString(R.string.camera_state_connected))) {
            tvStatus.setText(context.getString(R.string.camera_state_connecting));
        }
    }
    
    public void update()
    {
        // Update state to connected
        if (!tvStatus.getText().equals(context.getString(R.string.camera_state_connected))) {
            tvStatus.setText(context.getString(R.string.camera_state_connected));
        }
        
        // Update recording colour
        Button bRecording = (Button) activity.findViewById(R.id.record);
        if ((Boolean) vars.get("recording")) {
            bRecording.setTextColor(Color.RED);
        } else {
            bRecording.setTextColor(Color.BLACK);
        }
        
        // Update image
        Drawable drawable = (Drawable) vars.get("drawable");
        ImageView ivCamera = (ImageView) activity.findViewById(R.id.camera);
        ivCamera.setImageDrawable(drawable);

        // Fps counter & bandwidth
        displayStats((Double) vars.get("fpsCount"), (Double) vars.get("bandwidth"));
    }
    
    protected void displayStats(Double fpsCount, Double bandwidth)
    {
        // Fps counter
        if (fpsCount != null) {
            TextView tvFpsCount = (TextView) activity.findViewById(R.id.fps_counter);
            tvFpsCount.setText(
                String.format(
                    context.getString(R.string.camera_fps_count),
                    Math.round(fpsCount)
                )
            );
        }
        
        // Bandwidth
        if (bandwidth != null) {
            TextView tvBandwidth = (TextView) activity.findViewById(R.id.bandwidth);
            tvBandwidth.setText(
                String.format(
                    context.getString(R.string.camera_bandwidth),
                    Math.round(bandwidth)
                )
            );
        }
    }
    
    public void error(Exception e)
    {
        // Update state to unable to connect
        if (e instanceof ClientException) {
            tvStatus.setText(context.getString(R.string.camera_state_unable_to_connect));
        }
        
        // Reset stats
        displayStats(0.0, 0.0);
        
        e.printStackTrace();
    }
}