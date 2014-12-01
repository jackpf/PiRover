package com.jackpf.pirover.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.MainActivity;

import java.util.Observer;
import java.util.Observable;

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

    private class ControllerObserver implements Observer
    {
        public boolean hasBeenNotified = false;

        public void update(Observable observable, Object data) {
            hasBeenNotified = true;
        }
    }
    
    public void testLazyUpdate() throws Exception
    {
        Controller controller = new Controller();
        ControllerObserver observer = new ControllerObserver();
        controller.addObserver(observer);
        
        assertFalse(observer.hasBeenNotified);
        
        controller.setSteeringPosition(10);

        assertTrue(observer.hasBeenNotified);
        observer.hasBeenNotified = false;
        
        controller.setSteeringPosition(10);

        assertFalse(observer.hasBeenNotified);
    }
}
