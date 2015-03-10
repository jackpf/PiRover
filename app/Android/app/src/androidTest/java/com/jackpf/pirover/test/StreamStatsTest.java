package com.jackpf.pirover.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.Camera.StreamStats;
import com.jackpf.pirover.MainActivity;

public class StreamStatsTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private Context context;

    public StreamStatsTest()
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
    
    public void testStatsCalculator() throws Exception
    {
        int bytes[] = {138, 142, 178, 198};
        int bytesSum = 0;
        StreamStats stats = new StreamStats();

        for (int i = 0; i < bytes.length; i++) {
            stats.addFrame(bytes[i]);
            bytesSum += bytes[i];
        }

        // Need to artificially add one after a bit so the fps is averaged out
        Thread.sleep(1000);
        stats.addFrame(1);

        assertEquals(Math.round(stats.getFps()), (double) bytes.length + 1);
        assertEquals(stats.getBandwidth(), (double) bytesSum / bytes.length);
    }
}
