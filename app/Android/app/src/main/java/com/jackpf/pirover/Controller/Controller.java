package com.jackpf.pirover.Controller;

import java.util.Observable;

public class Controller extends Observable
{
    /**
     * Accelerator position
     */
    private int acceleratorPosition;
    
    /**
     * Steering wheel position
     */
    private int steeringPosition;
    
    /**
     * Set accelerator position
     * 
     * @param acceleratorPosition
     */
    public void setAcceleratorPosition(int acceleratorPosition)
    {
        if (acceleratorPosition != this.acceleratorPosition) {
            setChanged();
        }

        this.acceleratorPosition = acceleratorPosition;

        notifyObservers();
    }
    
    /**
     * Get accelerator position
     * 
     * @return
     */
    public int getAcceleratorPosition()
    {
        return this.acceleratorPosition;
    }
    
    /**
     * Set steering wheel position
     * 
     * @param steeringPosition
     */
    public void setSteeringPosition(int steeringPosition)
    {
        if (steeringPosition != this.steeringPosition) {
            setChanged();
        }

        this.steeringPosition = steeringPosition;

        notifyObservers();
    }
    
    /**
     * Get steering wheel position
     * 
     * @return
     */
    public int getSteeringPosition()
    {
        return this.steeringPosition;
    }
}
