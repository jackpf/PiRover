package com.jackpf.pirover.Controller;

public class ControllerCalculator
{
    /**
     * Calculate steering wheel value from event
     *
     * @param x                 X position of touch
     * @param y                 Y position of touch
     * @param width             Width of drawable container
     * @param height            Height of drawable container
     * @return                  Calculated steering value
     */
    public static int calculateSteeringValue(float x, float y, int width, int height)
    {
        x -= (width / 2);
        y = -(y - (height / 2));

        if (y < 0.0) {
            y = 0.0f;
        }

        double angle = Math.toDegrees(Math.atan(y / x));

        if (x >= 0.0) {
            angle = 90.0 - angle;
        } else {
            angle = -(90.0 + angle);
        }

        int value = (int) Math.round(angle / 9.0);

        return value;
    }

    /**
     * Calculate steering wheel position from value
     *
     * @param steeringValue     Controller steering wheel value
     * @return                  Position of steering wheel
     */
    public static int calculateSteeringPosition(int steeringValue)
    {
        return (int) (steeringValue * 9.0);
    }

    /**
     * Calculate accelerator value
     *
     * @param y                 Y position of touch
     * @param width             Width of drawable container
     * @param height            Height of drawable container
     * @param drawableHeight    Height of drawable
     * @return                  Calculated accelerator value
     */
    public static int calculateAcceleratorValue(float y, int width, int height, int drawableHeight)
    {
        height -= drawableHeight;
        y = height - (y - (drawableHeight / 2));

        if (y < 0) {
            y = 0.0f;
        } else if (y > height) {
            y = height;
        }

        int value = (int) Math.round(y * 10.0 / height);

        return value;
    }

    /**
     * Calculate accelerator position
     *
     * @param acceleratorValue  Controller acceleration value
     * @param height            Height of drawable container
     * @param drawableHeight    Height of drawable
     * @return                  Position of accelerator
     */
    public static int calculateAcceleratorPosition(int acceleratorValue, int height, int drawableHeight)
    {
        height -= drawableHeight;
        return height - (int) (acceleratorValue * (height / 10.0));
    }
}
