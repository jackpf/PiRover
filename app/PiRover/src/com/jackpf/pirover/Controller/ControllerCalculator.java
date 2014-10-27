package com.jackpf.pirover.Controller;

public class ControllerCalculator
{
    /**
     * Position struct
     */
    public static class Position
    {
        /**
         * Calculated value to send to controller
         */
        public final int value;
        
        /**
         * Calculated position of the UI element/drawable
         */
        public final int position;
        
        /**
         * Set value and position
         * 
         * @param value
         * @param position
         */
        public Position(int value, int position)
        {
            this.value = value;
            this.position = position;
        }
    }
    
    /**
     * Calculate steering wheel position and value
     * 
     * @param x                 X position of touch
     * @param y                 Y position of touch
     * @param width             Width of drawable container
     * @param height            Height of drawable container
     * @return                  Calculated position values
     */
    public static Position calculateSteeringPosition(float x, float y, int width, int height)
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
        int roundedAngle = (int) (value * 9.0);
        
        return new Position(value, roundedAngle);
    }
    
    /**
     * Calculate accelerator position and value
     * 
     * @param y                 Y position of touch
     * @param width             Width of drawable container
     * @param height            Height of drawable container
     * @param drawableHeight    Height of drawable
     * @return                  Calculated position values
     */
    public static Position calculateAcceleratorPosition(float y, int width, int height, int drawableHeight)
    {
        height -= drawableHeight;
        y = height - (y - (drawableHeight / 2));
        
        if (y < 0) {
            y = 0;
        } else if (y > height) {
            y = height;
        }
        
        int value = (int) Math.round(y * 10.0 / height);
        int roundedY = (int) (value * (height / 10.0));
        
        return new Position(value, height - roundedY);
    }
}
