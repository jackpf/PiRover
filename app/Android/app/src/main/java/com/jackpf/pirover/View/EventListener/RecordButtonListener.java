package com.jackpf.pirover.View.EventListener;

import android.view.View;

import com.jackpf.pirover.Camera.Recorder;

public class RecordButtonListener implements View.OnClickListener
{
    private Recorder recorder;
    
    public RecordButtonListener(Recorder recorder)
    {
        this.recorder = recorder;
    }
    
    @Override
    public void onClick(View v)
    {
        recorder.toggleRecording();
    }
}
