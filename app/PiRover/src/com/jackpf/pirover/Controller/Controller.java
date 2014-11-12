package com.jackpf.pirover.Controller;


public class Controller
{
    private int acceleratorPosition;
    private int steeringPosition;
    private boolean pendingUpdate;
    
    private void pendingUpdate(int a, int b)
    {
        pendingUpdate = pendingUpdate || a != b;
    }
    
    public boolean consumeUpdate()
    {
        boolean consumed = pendingUpdate;
        pendingUpdate = false;
        
        return consumed;
    }
    
    public void setAcceleratorPosition(int acceleratorPosition)
    {
        pendingUpdate(acceleratorPosition, this.acceleratorPosition);
        this.acceleratorPosition = acceleratorPosition;
    }
    
    public int getAcceleratorPosition()
    {
        return this.acceleratorPosition;
    }
    
    public void setSteeringPosition(int steeringPosition)
    {
        pendingUpdate(steeringPosition, this.steeringPosition);
        this.steeringPosition = steeringPosition;
    }
    
    public int getSteeringPosition()
    {
        return this.steeringPosition;
    }
}
