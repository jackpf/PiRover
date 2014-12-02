package com.jackpf.pirover.View.EventListener;

import android.view.MotionEvent;
import android.view.View;

import com.jackpf.pirover.PlaybackActivity;

public class PlaybackControlListener implements View.OnTouchListener
{
    private PlaybackActivity activity;

    public PlaybackControlListener(PlaybackActivity activity)
    {
        this.activity = activity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e)
    {
        v.performClick();

        activity.setPosition(e.getX() / v.getWidth());

        return false;
    }
}
