package com.jackpf.pirover.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Controller.ControllerCalculator;

public class ControllerCalculatorTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private Context context;
    
    public ControllerCalculatorTest()
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
    
    public void testSteeringWheel() throws Exception
    {
        ControllerCalculator.Position position;
        final int WIDTH = 384, HEIGHT = 384;
        
        // Center of steering wheel
        position = ControllerCalculator
            .calculateSteeringPosition(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
        assertEquals(position.value, 0);
        assertEquals(position.position, 0);
        
        // 45 degree angle
        position = ControllerCalculator
            .calculateSteeringPosition(WIDTH, 0, WIDTH, HEIGHT);
        assertEquals(position.value, 5);
        assertEquals(position.position, 45);
        
        // -45 degree angle
        position = ControllerCalculator
            .calculateSteeringPosition(0, 0, WIDTH, HEIGHT);
        assertEquals(position.value, -5);
        assertEquals(position.position, -45);
        
        // 90 degree angle
        position = ControllerCalculator
            .calculateSteeringPosition(WIDTH, HEIGHT / 2, WIDTH, HEIGHT);
        assertEquals(position.value, 10);
        assertEquals(position.position, 90);
        
        // 90+ degree angle
        position = ControllerCalculator
            .calculateSteeringPosition(WIDTH, HEIGHT / 2 + 100, WIDTH, HEIGHT);
        assertEquals(position.value, 10);
        assertEquals(position.position, 90);
    }
    
    public void testAccelerator() throws Exception
    {
        ControllerCalculator.Position position;
        final int WIDTH = 96, HEIGHT = 384, INTRINSIC_HEIGHT = 96;

        // 0 acceleration
        position = ControllerCalculator
            .calculateAcceleratorPosition(HEIGHT, WIDTH, HEIGHT, INTRINSIC_HEIGHT);
        assertEquals(position.value, 0);
        assertEquals(position.position, HEIGHT - INTRINSIC_HEIGHT);

        // Full acceleration
        position = ControllerCalculator
            .calculateAcceleratorPosition(0, WIDTH, HEIGHT, INTRINSIC_HEIGHT);
        assertEquals(position.value, 10);
        assertEquals(position.position, 0);

        // Half acceleration
        position = ControllerCalculator
            .calculateAcceleratorPosition(HEIGHT / 2, WIDTH, HEIGHT, INTRINSIC_HEIGHT);
        assertEquals(position.value, 5);
        assertEquals(position.position, (HEIGHT - INTRINSIC_HEIGHT) / 2);
    }
}
