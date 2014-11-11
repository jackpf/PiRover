package com.jackpf.pirover.View.EventListener;

import android.app.Activity;
import android.view.View;

import com.jackpf.pirover.MainActivity;

public class RecordButtonListener implements View.OnClickListener
{
    private Activity activity;
    
    public RecordButtonListener(Activity activity)
    {
        this.activity = activity;
    }
    
    @Override
    public void onClick(View v)
    {
        ((MainActivity) activity).startPlaybackActivity();
    }
}
