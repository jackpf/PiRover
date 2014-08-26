package com.jackpf.pirover.Camera;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client
{
    private Socket socket;
    
    public Client(String host, int port) throws IOException
    {
        socket = new Socket(host, port);
    }
    
    public byte[] getFrame() throws IOException
    {
        InputStream is = socket.getInputStream();

        // The first 32 bit int (4 bytes) of the stream will be the size of the image
        byte[] szBuf = new byte[4];
        is.read(szBuf, 0, 4);
        int sz = byteArrayToInt(szBuf);
        
        byte[] buffer = new byte[sz], image = new byte[sz];
        int bytesRead = 0, bytesReadTotal = 0, bytesWritten = 0;
        
        // Read the image data
        while (bytesReadTotal < sz) {
            bytesRead = is.read(buffer, 0, sz - bytesReadTotal);
            bytesReadTotal += bytesRead;
            
            for (int i = 0; i < bytesRead; i++, bytesWritten++) {
                image[bytesWritten] = buffer[i];
            }
        }
        
        return image;
    }
    
    protected int byteArrayToInt(byte[] bytes)
    {
        int int32 = 0;
        
        for (int i = 0; i < bytes.length; i++) {
            int32 += (bytes[i] & 0xFF) << (8 * i);
        }
        
        return int32;
    }
}
