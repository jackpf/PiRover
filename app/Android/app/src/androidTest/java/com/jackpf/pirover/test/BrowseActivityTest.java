package com.jackpf.pirover.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.jackpf.pirover.BrowseActivity;
import com.jackpf.pirover.MainActivity;

public class BrowseActivityTest extends ActivityUnitTestCase<BrowseActivity>
{
    private Intent intent;

    public BrowseActivityTest()
    {
        super(BrowseActivity.class);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        intent = new Intent(
            getInstrumentation().getTargetContext(),
            BrowseActivity.class
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
