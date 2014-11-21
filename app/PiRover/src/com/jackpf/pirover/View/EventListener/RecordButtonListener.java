package com.jackpf.pirover.View.EventListener;

import android.view.View;

import com.jackpf.pirover.MainActivity;

public class RecordButtonListener implements View.OnClickListener
{
    private MainActivity activity;
    
    public RecordButtonListener(MainActivity activity)
    {
        this.activity = activity;
    }
    
    @Override
    public void onClick(View v)
    {
        activity.toggleRecording();
    }
}
