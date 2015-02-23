package com.jackpf.pirover;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.DrawableFrameFactory;
import com.jackpf.pirover.Camera.FpsCalculator;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Request.PlaybackRequest;
import com.jackpf.pirover.RequestThread.Callback;
import com.jackpf.pirover.View.EventListener.PlaybackControlObserver;
import com.jackpf.pirover.View.PlaybackUI;

import java.io.File;
import java.io.FileNotFoundException;

public class PlaybackActivity extends Activity
{
    protected RequestThread thread;
    protected Request playbackRequest;
    protected BufferedVideo video;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_playback);
        
        try {
            video = new BufferedVideo(new DrawableFrameFactory(), getIntent().getStringExtra("video"));

            int fpsCalculatorStrategy = Integer.parseInt(
                preferences.getString(getString(R.string.pref_fps_key), getString(R.string.pref_fps_default))
            );

            switch (fpsCalculatorStrategy) {
                case FpsCalculator.REPLICATE:
                    video.setFpsCalculator(new FpsCalculator.ReplicateStrategy());
                    break;
                case FpsCalculator.MEAN:
                    video.setFpsCalculator(new FpsCalculator.SmoothMeanStrategy());
                    break;
                case FpsCalculator.MEDIAN:
                    video.setFpsCalculator(new FpsCalculator.SmoothMedianStrategy());
                    break;
                case FpsCalculator.MODE:
                    video.setFpsCalculator(new FpsCalculator.SmoothModalStrategy());
                    break;
                case FpsCalculator.SPECIFY:
                    video.setFpsCalculator(new FpsCalculator.SpecifyStrategy(Integer.parseInt(preferences.getString(getString(R.string.pref_fps_specify_key), getString(R.string.pref_fps_specify_default)))));
                    break;
            }
        } catch (FileNotFoundException e) {
            finish();
        }
        
        playbackRequest = new PlaybackRequest(video);

        initialiseUI(new PlaybackUI(this));

        video.addObserver(new PlaybackControlObserver(this));
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
        getMenuInflater().inflate(R.menu.playback, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.text_delete_confirm))
                    .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            new File(getIntent().getStringExtra("video")).delete();
                            finish();
                        }
                    })
                    .setNegativeButton(getString(android.R.string.no), null)
                    .show();
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

        final Handler handler = new Handler();
        
        thread = new RequestThread(
            playbackRequest,
            getUI(PlaybackUI.class)
        ).setCallback(new Callback() {
            public void onPostExecute(RequestResponse vars, Exception e) {
                if (video.isPlaying() && vars.get("drawable") != null && e == null) {
                    int fps = (Integer) vars.get("fps");
                    int delay = 1000 / fps;

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
    
    public void playbackControl(View v)
    {
        // State vars
        int direction       = video.getDirection();
        boolean isPlaying   = video.isPlaying();
        
        switch (v.getId()) {
            case R.id.play:
                direction = BufferedVideo.PLAY;
                isPlaying = !isPlaying;
            break;
            case R.id.rewind:
                direction = BufferedVideo.REWIND;
                isPlaying = true;
            break;
            case R.id.fastforward:
                direction = BufferedVideo.FASTFORWARD;
                isPlaying = true;
            break;
        }
        
        video.setDirection(direction);
        video.isPlaying(isPlaying);
        
        if (isPlaying) {
            executePlaybackRequest();
        }
    }
    
    public void setPosition(float ratio)
    {
        video.setFramePosition((int) Math.round((video.getFrameCount() - 1) * ratio));

        // Load 1 frame to update the view to the current position
        if (!video.isPlaying()) {
            video.isPlaying(true);
            executePlaybackRequest();
            video.isPlaying(false);
        }
    }
}
