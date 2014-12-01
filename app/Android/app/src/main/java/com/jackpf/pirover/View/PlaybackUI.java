package com.jackpf.pirover.View;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.PlaybackActivity;
import com.jackpf.pirover.R;

public class PlaybackUI extends UI<PlaybackActivity>
{
    private ImageView ivCamera;
    private ProgressDialog dialog;
    private ProgressBar pPlayback;
    
    public PlaybackUI(PlaybackActivity activity)
    {
        super(activity);
    }

    @Override
    public void initialise()
    {
        ivCamera = (ImageView) activity.findViewById(R.id.camera);
        pPlayback = (ProgressBar) activity.findViewById(R.id.video_progress);
        
        dialog = new ProgressDialog(context);
        
        dialog.setTitle(context.getString(R.string.playback_loading_title));
        dialog.setMessage(context.getString(R.string.playback_loading_text));
        dialog.setCancelable(false);
        
        dialog.show();
        
        pPlayback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                v.performClick();
                
                activity.setPosition(e.getX() / v.getWidth());
                
                return false;
            }
        });
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

        int currentFrame = (Integer) vars.get("current_frame");
        int frameCount = (Integer) vars.get("frame_count");
        int progress = (int) Math.round(currentFrame * 100.0 / frameCount);
        pPlayback.setProgress(progress);
    }
}