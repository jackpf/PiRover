package com.jackpf.pirover.Camera;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Video
{
    private List<Frame> frames = new ArrayList<Frame>();
    private LocalClient client;
    private int framePosition = 0;
    
    public Video(String filename) throws FileNotFoundException
    {
        client = new LocalClient(filename);
    }
    
    public Video load()
    {
        Frame frame;
        
        do {
            try {
                frame = client.getFrame();
                frames.add(frame);
            } catch (ClientException e) {
                frame = null;
            }
        } while (frame != null);
        
        return this;
    }
    
    public Frame getFrame()
    {
        if (framePosition >= frames.size()) {
            return null;
        }
        
        Frame frame = frames.get(framePosition);
        framePosition++;
        
        return frame;
    }
    
    public int getFramePosition()
    {
        return framePosition;
    }
    
    public int getFrameCount()
    {
        return frames.size();
    }
}
