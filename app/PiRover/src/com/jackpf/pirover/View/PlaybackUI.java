package com.jackpf.pirover.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Model.UI;

public class PlaybackUI extends UI
{
    private ImageView ivCamera;
    
    public PlaybackUI(Context context)
    {
        super(context);
    }

    @Override
    public void initialise()
    {
        ivCamera = (ImageView) activity.findViewById(R.id.camera);
    }

    @Override
    public void update()
    {
        // Update image
        if (vars.get("finished") == null || !(Boolean) vars.get("finished")) {
            Drawable drawable = (Drawable) vars.get("drawable");
            ivCamera.setImageDrawable(drawable);
        }
    }
}