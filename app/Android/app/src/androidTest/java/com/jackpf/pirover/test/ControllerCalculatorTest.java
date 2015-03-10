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
        int position, value;
        final int WIDTH = 384, HEIGHT = 384;
        
        // Center of steering wheel
        value = ControllerCalculator
            .calculateSteeringValue(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
        position = ControllerCalculator
            .calculateSteeringPosition(value);
        assertEquals(value, 0);
        assertEquals(position, 0);
        
        // 45 degree angle
        value = ControllerCalculator
            .calculateSteeringValue(WIDTH, 0, WIDTH, HEIGHT);
        position = ControllerCalculator
            .calculateSteeringPosition(value);
        assertEquals(value, 5);
        assertEquals(position, 45);
        
        // -45 degree angle
        value = ControllerCalculator
            .calculateSteeringValue(0, 0, WIDTH, HEIGHT);
        position = ControllerCalculator
            .calculateSteeringPosition(value);
        assertEquals(value, -5);
        assertEquals(position, -45);
        
        // 90 degree angle
        value = ControllerCalculator
            .calculateSteeringValue(WIDTH, HEIGHT / 2, WIDTH, HEIGHT);
        position = ControllerCalculator
            .calculateSteeringPosition(value);
        assertEquals(value, 10);
        assertEquals(position, 90);
        
        // 90+ degree angle
        value = ControllerCalculator
            .calculateSteeringValue(WIDTH, HEIGHT / 2 + 100, WIDTH, HEIGHT);
        position = ControllerCalculator
            .calculateSteeringPosition(value);
        assertEquals(value, 10);
        assertEquals(position, 90);
    }
    
    public void testAccelerator() throws Exception
    {
        int value, position;
        final int WIDTH = 96, HEIGHT = 384, INTRINSIC_HEIGHT = 96;

        // 0 acceleration
        value = ControllerCalculator
            .calculateAcceleratorValue(HEIGHT, WIDTH, HEIGHT, INTRINSIC_HEIGHT);
        position = ControllerCalculator
            .calculateAcceleratorPosition(value, HEIGHT, INTRINSIC_HEIGHT);
        assertEquals(value, 0);
        assertEquals(position, HEIGHT - INTRINSIC_HEIGHT);

        // Full acceleration
        value = ControllerCalculator
            .calculateAcceleratorValue(0, WIDTH, HEIGHT, INTRINSIC_HEIGHT);
        position = ControllerCalculator
            .calculateAcceleratorPosition(value, HEIGHT, INTRINSIC_HEIGHT);
        assertEquals(value, 10);
        assertEquals(position, 0);

        // Half acceleration
        value = ControllerCalculator
            .calculateAcceleratorValue(HEIGHT / 2, WIDTH, HEIGHT, INTRINSIC_HEIGHT);
        position = ControllerCalculator
            .calculateAcceleratorPosition(value, HEIGHT, INTRINSIC_HEIGHT);
        assertEquals(value, 5);
        assertEquals(position, (HEIGHT - INTRINSIC_HEIGHT) / 2);
    }
}
