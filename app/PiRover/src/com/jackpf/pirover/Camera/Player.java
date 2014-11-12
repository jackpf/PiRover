package com.jackpf.pirover.Camera;

public class Player
{
    private boolean isPlaying;
    
    public Player(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
    }
    
    public void toggleIsPlaying()
    {
        isPlaying = !isPlaying;
    }
    
    public boolean isPlaying()
    {
        return isPlaying;
    }
}
