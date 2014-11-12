package com.jackpf.pirover;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.jackpf.pirover.NetworkThread.Callback;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.Request.PlaybackRequest;
import com.jackpf.pirover.View.PlaybackUI;

public class PlaybackActivity extends Activity
{
    protected NetworkThread thread;
    protected UI<PlaybackActivity> playbackUI;
    protected /*Request*/PlaybackRequest playbackRequest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_playback);
        
        playbackRequest = new PlaybackRequest();

        playbackUI = new PlaybackUI(this);
        playbackUI.initialise();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        executePlaybackRequest();
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Runs the network thread and updates the UI
     * Sectioned off since it's called from onCreate and from the refresh button
     */
    protected void executePlaybackRequest()
    {
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
        
        thread = new NetworkThread(
            playbackRequest,
            playbackUI
        ).setCallback(new Callback() {
            public void onPostExecute(RequestResponse vars, Exception e) {
                if (vars.get("drawable") != null && e == null) {
                    int fps = (Integer) vars.get("fps");
                    int delay = 1000 / fps;
                    
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            executePlaybackRequest();
                        }
                    }, delay);
                }
            }
        });
        
        thread.execute();
    }
}
