package com.jackpf.pirover.View.EventListener;

import android.view.MotionEvent;
import android.view.View;

import com.jackpf.pirover.Controller.Launcher;
import com.jackpf.pirover.R;

public class LauncherListener implements View.OnTouchListener
{
    private Launcher launcher;

    public LauncherListener(Launcher launcher)
    {
        this.launcher = launcher;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        v.performClick();

        switch (v.getId()) {
            case R.id.launcher_right:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    launcher.right();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    launcher.stop();
                }
            break;
            case R.id.launcher_left:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    launcher.left();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    launcher.stop();
                }
            break;
            case R.id.launcher_up:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    launcher.up();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    launcher.stop();
                }
            break;
            case R.id.launcher_down:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    launcher.down();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    launcher.stop();
                }
            break;
            case R.id.launcher_fire:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    launcher.fire();
                }
            break;
        }
        
        return true;
    }
}
