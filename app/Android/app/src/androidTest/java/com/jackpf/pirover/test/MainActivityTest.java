package com.jackpf.pirover.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.jackpf.pirover.MainActivity;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity>
{
    private Intent intent;

    public MainActivityTest()
    {
        super(MainActivity.class);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        intent = new Intent(
            getInstrumentation().getTargetContext(),
            MainActivity.class
        );
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
