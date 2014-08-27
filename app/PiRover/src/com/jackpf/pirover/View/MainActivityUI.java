package com.jackpf.pirover.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Model.UIInterface;

public class MainActivityUI extends UIInterface
{
    public MainActivityUI(Context context)
    {
        super(context);
    }
    
    public void initialise()
    {
        
    }
    
    public void preUpdate()
    {
        
    }
    
    public void update()
    {
        Drawable drawable = (Drawable) vars.get("drawable");
        ImageView ivCamera = (ImageView) ((Activity) context).findViewById(R.id.camera);
        ivCamera.setImageDrawable(drawable);

        Double fpsCount = (Double) vars.get("fpsCount");
        if (fpsCount != null) {
            TextView tvFpsCount = (TextView) ((Activity) context).findViewById(R.id.fps_counter);
            tvFpsCount.setText(Math.round(fpsCount) + " FPS");
        }
        
        Double bandwidth = (Double) vars.get("bandwidth");
        if (bandwidth != null) {
            TextView tvBandwidth = (TextView) ((Activity) context).findViewById(R.id.bandwidth);
            tvBandwidth.setText(Math.round(bandwidth) + " bytes/s");
        }
    }
    
    public void error(Exception e)
    {
        e.printStackTrace();
    }
}