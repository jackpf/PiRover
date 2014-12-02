package com.jackpf.pirover.View.EventListener;

import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.PlaybackActivity;
import com.jackpf.pirover.R;

import java.util.Observable;
import java.util.Observer;

public class PlaybackControlObserver implements Observer
{
    private ImageButton btnPlay;
    private ProgressBar pPlayback;

    public PlaybackControlObserver(PlaybackActivity activity)
    {
        btnPlay = (ImageButton) activity.findViewById(R.id.play);
        pPlayback = (ProgressBar) activity.findViewById(R.id.video_progress);
    }

    @Override
    public void update(Observable observable, Object data)
    {
        BufferedVideo video = (BufferedVideo) observable;
        btnPlay.setSelected(!video.isPlaying());

        int progress = (int) Math.round(video.getFramePosition() * 100.0 / video.getFrameCount());
        pPlayback.setProgress(progress);
    }
}
