package com.jackpf.pirover.test;

import android.content.Context;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.DrawableFrameFactory;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Request.PlaybackRequest;

import java.io.File;

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

    protected BufferedVideo loadTestVideo() throws Exception
    {
        String filename = Environment.getExternalStorageDirectory() + "/" + Recorder.RECORD_DIR + "/test.prr";

        if (!new File(filename).exists()) {
            throw new Exception("Test file not present");
        }

        return new BufferedVideo(new DrawableFrameFactory(), filename);
    }
    
    public void testVideoLoad() throws Exception
    {
        BufferedVideo video = loadTestVideo();

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
        BufferedVideo video = loadTestVideo();

        RequestResponse params = new PlaybackRequest(video).call();

        assertNotNull(params);
        assertNotNull(params.get("drawable"));
    }

    public void testFrameCounter() throws Exception
    {
        BufferedVideo video = loadTestVideo();

        video.load();

        assertEquals(video.getFramePosition(), -1);

        while (video.getFrame() != null);

        assertEquals(video.getFramePosition(), video.getFrameCount());
    }

    public void testFastForward() throws Exception
    {
        BufferedVideo video = loadTestVideo();

        video.load();

        assertEquals(video.getFramePosition(), -1);

        video.setDirection(BufferedVideo.FASTFORWARD);

        // Fastforward
        int expectedFrames = (int) Math.round(video.getFrameCount() / BufferedVideo.SKIP_FRAMES), frames = 0;

        while (video.getFrame() != null) {
            frames++;
        }

        assertEquals(expectedFrames, frames);
        assertEquals(video.getFramePosition(), video.getFrameCount());

        // Rewind
        video.setDirection(BufferedVideo.REWIND);

        frames = 0;

        while (video.getFrame() != null) {
            frames++;
        }

        assertEquals(expectedFrames, frames);
        assertEquals(video.getFramePosition(), -1);
    }
}
