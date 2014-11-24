package com.jackpf.pirover.Camera;


public class StreamStats
{
    private int frames = 0;
    private long startTime = 0, endTime = 0;
    private static final int INTERVAL = 1000;
    private double fps;
    private long bytes;
    private double bandwidth;
    
    public void addFrame(int bytes)
    {
        this.bytes += bytes;
        frames++;
        
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - startTime > INTERVAL) {
            endTime = System.currentTimeMillis();
            double elapsedTime = (endTime - startTime) / 1000.0;
            
            fps = (double) (frames / elapsedTime);
            bandwidth = (double) (this.bytes / elapsedTime);
            
            startTime = endTime = frames = 0;
            this.bytes = 0;
        }
    }
    
    public double getFps()
    {
        return fps;
    }
    
    public double getBandwidth()
    {
        return bandwidth;
    }
}
