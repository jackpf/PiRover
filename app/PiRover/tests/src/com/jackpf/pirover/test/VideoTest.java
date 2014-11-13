package com.jackpf.pirover.test;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Request.PlaybackRequest;

public class VideoTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private Context context;
    
    public VideoTest()
    {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        context = getInstrumentation().getContext();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testVideoLoad() throws Exception
    {
        String filename = Environment.getExternalStorageDirectory() + "/" + Recorder.RECORD_DIR + "/test.prr";
        
        if (!new File(filename).exists()) {
            throw new Exception("Test file not present");
        }
        
        BufferedVideo video = new BufferedVideo(filename);

        assertFalse(video.isLoaded());
        assertEquals(video.getFrameCount(), 0);
        assertNull(video.getFrame());
        assertEquals(video.getDirection(), BufferedVideo.PLAY);
        assertFalse(video.isPlaying());
        
        video.load();
        
        assertTrue(video.isLoaded());
        assertTrue(video.getFrameCount() > 0);
        assertNotNull(video.getFrame());
    }
    
    public void testVideoRequest() throws Exception
    {
        String filename = Environment.getExternalStorageDirectory() + "/" + Recorder.RECORD_DIR + "/test.prr";
        
        if (!new File(filename).exists()) {
            throw new Exception("Test file not present");
        }
        
        BufferedVideo video = new BufferedVideo(filename);
        
        RequestResponse params = new PlaybackRequest(video).call();
        
        assertNotNull(params);
        assertNotNull(params.get("drawable"));
    }
}
