package com.jackpf.pirover.Controller;

import java.util.Observable;

public class Controller extends Observable
{
    /**
     * Acceleration
     */
    private int acceleration;
    
    /**
     * Steering
     */
    private int steering;
    
    /**
     * Set acceleration
     * 
     * @param acceleration
     */
    public void setAcceleration(int acceleration)
    {
        if (acceleration != this.acceleration) {
            setChanged();
        }

        this.acceleration = acceleration;

        notifyObservers();
    }
    
    /**
     * Get acceleration
     * 
     * @return
     */
    public int getAcceleration()
    {
        return this.acceleration;
    }
    
    /**
     * Set steering
     * 
     * @param steering
     */
    public void setSteering(int steering)
    {
        if (steering != this.steering) {
            setChanged();
        }

        this.steering = steering;

        notifyObservers();
    }
    
    /**
     * Get steering
     * 
     * @return
     */
    public int getSteering()
    {
        return this.steering;
    }
}
