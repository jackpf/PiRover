package com.jackpf.pirover.Controller;


public class Controller
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
     * Is update pending?
     */
    private boolean pendingUpdate;
    
    /**
     * Set pending update if an update is already pending or if a new value is set
     * 
     * @param a
     * @param b
     */
    private void setPendingUpdate(int a, int b)
    {
        pendingUpdate = pendingUpdate || a != b;
    }
    
    /**
     * Check if an update is pending, and set pending updates to false
     * 
     * @return True if an update is pending, false otherwise
     */
    public boolean consumeUpdate()
    {
        boolean consumed = pendingUpdate;
        pendingUpdate = false;
        
        return consumed;
    }
    
    /**
     * Set accelerator position
     * 
     * @param acceleratorPosition
     */
    public void setAcceleratorPosition(int acceleratorPosition)
    {
        setPendingUpdate(acceleratorPosition, this.acceleratorPosition);
        this.acceleratorPosition = acceleratorPosition;
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
        setPendingUpdate(steeringPosition, this.steeringPosition);
        this.steeringPosition = steeringPosition;
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
