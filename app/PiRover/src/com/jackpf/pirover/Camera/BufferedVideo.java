package com.jackpf.pirover.Camera;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BufferedVideo
{
    private List<Frame> frames = new ArrayList<Frame>();
    private LocalClient client;
    private int framePosition = 0;
    
    public static final int FASTFORWARD = 1, REWIND = -1, PLAY = 0;
    
    private String filename;
    private boolean isPlaying = true;
    private int direction = PLAY;
    
    public BufferedVideo(String filename) throws FileNotFoundException
    {
        client = new LocalClient(filename);
        this.filename = filename;
    }
    
    public boolean isLoaded()
    {
        return frames.size() > 0;
    }
    
    public BufferedVideo load()
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
        if (framePosition < 0 || framePosition >= frames.size()) {
            return null;
        }
        
        Frame frame = frames.get(framePosition);

        switch (direction) {
            case PLAY:
                framePosition++;
            break;
            case REWIND:
                framePosition = framePosition - 10 >= 0 ? framePosition - 10 : 0;
            break;
            case FASTFORWARD:
                framePosition = framePosition + 10 < frames.size() ? framePosition + 10 : frames.size();
            break;
        }
        
        return frame;
    }
    
    public void setFramePosition(int framePosition)
    {
        this.framePosition = framePosition;
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
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    public int getDirection()
    {
        return this.direction;
    }
    
    public void isPlaying(boolean isPlaying)
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
