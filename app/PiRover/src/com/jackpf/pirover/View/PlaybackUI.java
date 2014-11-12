package com.jackpf.pirover.View;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.jackpf.pirover.PlaybackActivity;
import com.jackpf.pirover.R;
import com.jackpf.pirover.Model.UI;

public class PlaybackUI extends UI<PlaybackActivity>
{
    private ImageView ivCamera;
    private ProgressDialog dialog;
    
    public PlaybackUI(PlaybackActivity activity)
    {
        super(activity);
    }

    @Override
    public void initialise()
    {
        ivCamera = (ImageView) activity.findViewById(R.id.camera);
        
        dialog = new ProgressDialog(context);
        
        dialog.setTitle(context.getString(R.string.playback_loading_title));
        dialog.setMessage(context.getString(R.string.playback_loading_text));
        dialog.setCancelable(false);
        
        dialog.show();
    }

    @Override
    public void update()
    {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        
        // Update image
        if (vars.get("drawable") != null) {
            Drawable drawable = (Drawable) vars.get("drawable");
            ivCamera.setImageDrawable(drawable);
        }
    }
}