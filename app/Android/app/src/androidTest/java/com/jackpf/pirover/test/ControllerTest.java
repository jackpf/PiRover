package com.jackpf.pirover.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Controller.Controller;

public class ControllerTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private Context context;
    
    public ControllerTest()
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
    
    public void testLazyUpdate() throws Exception
    {
        Controller controller = new Controller();
        
        assertFalse(controller.consumeUpdate());
        
        controller.setSteeringPosition(10);
        
        assertTrue(controller.consumeUpdate());
        
        controller.setSteeringPosition(10);
        
        assertFalse(controller.consumeUpdate());
    }
}
