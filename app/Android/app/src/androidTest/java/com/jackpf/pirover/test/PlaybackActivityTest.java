package com.jackpf.pirover.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.jackpf.pirover.PlaybackActivity;

public class PlaybackActivityTest extends ActivityUnitTestCase<PlaybackActivity>
{
    private Intent intent;

    public PlaybackActivityTest()
    {
        super(PlaybackActivity.class);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        intent = new Intent(
            getInstrumentation().getTargetContext(),
            PlaybackActivity.class
        );

        intent.putExtra("video", "test.prr");

        startActivity(intent, null, null);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testMainActivity()
    {
        assertNotNull(intent);
    }
}
