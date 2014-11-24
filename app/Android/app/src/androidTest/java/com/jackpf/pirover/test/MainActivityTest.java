package com.jackpf.pirover.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.jackpf.pirover.MainActivity;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity>
{
    private Context context;
    private MainActivity activity;
    private Intent intent;
    
    public MainActivityTest()
    {
        super(MainActivity.class);
        
        context = getInstrumentation().getContext();
        activity = getActivity();
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        Intent intent = new Intent(
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
