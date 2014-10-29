package com.jackpf.pirover.Controller;

import com.jackpf.pirover.NetworkThread;
import com.jackpf.pirover.Request.ControlRequest;

public class Controller
{
    private int acceleratorPosition;
    private int steeringPosition;
    private NetworkThread thread;
    private String ip, port;

    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
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
        
        thread.execute(ip, port);
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
