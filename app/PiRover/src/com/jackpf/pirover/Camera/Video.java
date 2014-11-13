package com.jackpf.pirover.Camera;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Video
{
    private List<Frame> frames = new ArrayList<Frame>();
    private LocalClient client;
    private int framePosition = 0;
    private String filename;
    
    public Video(String filename) throws FileNotFoundException
    {
        client = new LocalClient(filename);
        this.filename = filename;
    }
    
    public Video load()
    {
        Frame frame;
        
        do {
            try {
                frame = client.getFrame();
                
                if (frame != null) {
                    frames.add(frame);
                }
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
    
    public String getName()
    {
        return this.filename;
    }
    
    public Frame getIcon()
    {
        // Actually consumes the input stream, so can not be called if we've already loaded the stream
        if (frames.size() > 0) {
            return frames.get(0);
        }
        
        try {
            frames.add(client.getFrame());
            return frames.get(0);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
