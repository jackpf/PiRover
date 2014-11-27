package com.jackpf.pirover.Camera;

import com.jackpf.pirover.Service.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LocalClient extends Client
{
    private InputStream is;
    int fpsForLastFrame = -1;

    public LocalClient(FrameFactory frameFactory, String filename) throws FileNotFoundException
    {
        super(frameFactory, null);

        is = new FileInputStream(filename);
    }

    public Frame getFrame() throws ClientException
    {
        int fpsForLastFrame = getFpsForFrame();
        if (fpsForLastFrame != -1) {
            this.fpsForLastFrame = fpsForLastFrame;
        }

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

    public int getFpsForLastFrame() throws ClientException
    {
        int fps = fpsForLastFrame;

        if (fps == -1) {
            throw new ClientException("No fps data for frame");
        }

        fpsForLastFrame = -1;

        return fps;
    }
}
