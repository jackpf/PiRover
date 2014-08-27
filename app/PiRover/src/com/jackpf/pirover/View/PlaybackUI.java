package com.jackpf.pirover.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Model.UI;

public class PlaybackUI extends UI
{
    private TextView tvStatus;
    
    public PlaybackUI(Context context)
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
        // Update image
        if (vars.get("finished") == null || !(Boolean) vars.get("finished")) {
            Drawable drawable = (Drawable) vars.get("drawable");
            ImageView ivCamera = (ImageView) activity.findViewById(R.id.camera);
            ivCamera.setImageDrawable(drawable);
        }
    }
    
    public void error(Exception e)
    {
        e.printStackTrace();
    }
}