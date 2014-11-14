package com.jackpf.pirover.Camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LocalClient extends Client
{
    private InputStream is;
    
    public LocalClient(FrameFactory frameFactory, String filename) throws FileNotFoundException
    {
        super(frameFactory);
        
        is = new FileInputStream(filename);
    }
    
    public Frame getFrame() throws ClientException
    {
        return super.consumeFrameFromStream(is);
    }
}
