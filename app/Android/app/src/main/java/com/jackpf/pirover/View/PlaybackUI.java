package com.jackpf.pirover.View;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.PlaybackActivity;
import com.jackpf.pirover.R;
import com.jackpf.pirover.View.EventListener.PlaybackControlListener;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        ((ProgressBar) activity.findViewById(R.id.video_progress))
            .setOnTouchListener(new PlaybackControlListener(activity));
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
            System.err.println("NOT NULL");
        } else {
            /* Have to update here instead of in PlaybackControlObserver
               since it causes a CalledFromWrongThreadException when the video ends */
            ((ImageButton) activity.findViewById(R.id.play)).setSelected(true);
            System.err.println("NULL");
        }

        // Video info
        if (((TextView) activity.findViewById(R.id.filename_value)).getText() == "") {
            BufferedVideo.Info info = (BufferedVideo.Info) vars.get("info");

            ((TextView) activity.findViewById(R.id.filename_value)).setText(info.filename);
            ((TextView) activity.findViewById(R.id.date_value)).setText(
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(info.lastModified * 1000))
            );
            ((TextView) activity.findViewById(R.id.size_value)).setText(
                String.format(
                    context.getString(R.string.file_size),
                    (Math.round(info.size * 100.0) / 100.0)
                )
            );
        }

        // Update fps
        ((TextView) activity.findViewById(R.id.fps)).setText(
            String.format(
                context.getString(R.string.camera_fps_count),
                Math.round((Integer) vars.get("fps"))
            )
        );
    }
}