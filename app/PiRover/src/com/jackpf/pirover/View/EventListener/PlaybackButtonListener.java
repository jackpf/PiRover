package com.jackpf.pirover.View.EventListener;

import android.app.Activity;
import android.view.View;

import com.jackpf.pirover.MainActivity;

public class PlaybackButtonListener implements View.OnClickListener
{
    private Activity activity;
    
    public PlaybackButtonListener(Activity activity)
    {
        this.activity = activity;
    }
    
    @Override
    public void onClick(View v)
    {
        ((MainActivity) activity).toggleRecording();
    }
}
