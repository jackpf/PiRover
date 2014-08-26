package com.jackpf.pirover.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

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
        ImageView camera = (ImageView) ((Activity) context).findViewById(R.id.camera);
        camera.setImageDrawable(drawable);
    }
    
    public void error(Exception e)
    {
        e.printStackTrace();
    }
}