package com.jackpf.pirover.test;

import android.test.InstrumentationTestCase;

public class MainActivityTest extends InstrumentationTestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testMainActivity()
    {
        assertNotNull(getInstrumentation().getContext());
    }
}
