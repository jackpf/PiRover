package com.jackpf.pirover.Camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.jackpf.pirover.Service.Utils;

public class LocalClient extends Client
{
    private InputStream is;
    private int fpsSum = 0, framesRead = 0;
    
    public LocalClient(FrameFactory frameFactory, String filename) throws FileNotFoundException
    {
        super(frameFactory, null);
        
        is = new FileInputStream(filename);
    }
    
    public Frame getFrame() throws ClientException
    {
        framesRead++;
        fpsSum += getFpsForFrame();
        
        return super.consumeFrameFromStream(is);
    }
    
    protected int getFpsForFrame() throws ClientException
    {
        // Get the extra header information from the recorded stream
        try {
            byte[] fps = new byte[4];
            if (is.read(fps, 0, 4) >= 0) {
                return Utils.byteArrayToInt(fps);
            } else {
                return -1;
            }
        } catch (IOException e) {
            throw new ClientException("Unable to capture frame", e);
        }
    }
    
    public int getFps()
    {System.err.println(fpsSum+"/"+framesRead+" = "+Math.round(fpsSum / framesRead));
        return (int) Math.round(fpsSum / framesRead);
    }
}
