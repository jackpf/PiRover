package com.jackpf.pirover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Request.BrowseRequest;
import com.jackpf.pirover.View.BrowseUI;

public class BrowseActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        
        new RequestThread(new BrowseRequest(), new BrowseUI(this)).execute(
            Environment.getExternalStorageDirectory() + "/" + Recorder.RECORD_DIR
        );
    }
    
    public void startPlaybackActivity(BufferedVideo video)
    {
        Intent intent = new Intent(this, PlaybackActivity.class);
        intent.putExtra("video", video.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.browse, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }
}
