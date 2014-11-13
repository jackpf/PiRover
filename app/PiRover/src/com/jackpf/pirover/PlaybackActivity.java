package com.jackpf.pirover;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jackpf.pirover.RequestThread.Callback;
import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.Request.PlaybackRequest;
import com.jackpf.pirover.View.PlaybackUI;

public class PlaybackActivity extends Activity
{
    protected RequestThread thread;
    protected UI<PlaybackActivity> playbackUI;
    protected Request playbackRequest;
    protected BufferedVideo video;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_playback);
        
        try {
            video = new BufferedVideo(getIntent().getStringExtra("video"));
        } catch (FileNotFoundException e) {
            finish();
        }
        
        playbackRequest = new PlaybackRequest(video);

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

        video.isPlaying(false);
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
        if (thread instanceof RequestThread) {
            thread.cancel(true);
        }
        
        thread = new RequestThread(
            playbackRequest,
            playbackUI
        ).setCallback(new Callback() {
            public void onPostExecute(RequestResponse vars, Exception e) {
                if (video.isPlaying() && vars.get("drawable") != null && e == null) {
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
    
    public void togglePlayback(View v)
    {
        video.toggleIsPlaying();
        
        if (video.isPlaying()) {
            // Stop any rewind/fastforward
            video.setDirection(BufferedVideo.PLAY);
            executePlaybackRequest(); // Restart thread if we're playing again
        }
    }
    
    public void rewind(View v)
    {
        video.setDirection(BufferedVideo.REWIND);
        if (!video.isPlaying()) { // If not playing, set to play and start playing again
            video.isPlaying(true);
            executePlaybackRequest();
        }
    }
    
    public void fastForward(View v)
    {
        video.setDirection(BufferedVideo.FASTFORWARD);
        if (!video.isPlaying()) { // If not playing, set to play and start playing again
            video.isPlaying(true);
            executePlaybackRequest();
        }
    }
    
    public void setPosition(float ratio)
    {
        video.setFramePosition((int) Math.round(video.getFrameCount() * ratio));
        
        if (!video.isPlaying()) {
            video.isPlaying(true);
            executePlaybackRequest();
            video.isPlaying(false);
        }
    }
}
