package com.jackpf.pirover.Camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LocalClient extends Client
{
    private InputStream is;
    
    public LocalClient(String filename) throws FileNotFoundException
    {
        this.is = new FileInputStream(filename);
    }
    
    public Frame getFrame() throws ClientException
    {
        return super.consumeFrameFromStream(is);
    }
}
