package com.jackpf.pirover.Controller;

import com.jackpf.pirover.NetworkThread;
import com.jackpf.pirover.Request.ControlRequest;

public class Controller
{
    private int acceleratorPosition;
    private int steeringPosition;
    private NetworkThread thread;
    
    private void pendingUpdate(int a, int b)
    {
        if (a != b) {
            update();
        }
    }
    
    private void update()
    {
        if (thread instanceof NetworkThread) {
            thread.cancel(true);
        }
        
        thread = new NetworkThread(new ControlRequest(this));
        
        thread.execute();
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
