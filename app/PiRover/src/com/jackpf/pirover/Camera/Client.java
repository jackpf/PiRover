package com.jackpf.pirover.Camera;

import java.io.IOException;
import java.io.InputStream;

import com.jackpf.pirover.Service.Utils;

public class Client extends com.jackpf.pirover.Client.Client
{
    private StreamStats streamStats;
    
    public void connect(String host, int port) throws com.jackpf.pirover.Client.ClientException
    {
        super.connect(host, port);
        
        streamStats = new StreamStats();
    }
    
    public byte[] getFrame() throws ClientException
    {
        if (!isConnected()) {
            throw new ClientException("Client not connected");
        }
        
        try {
            byte[] image =  getFrame(socket.getInputStream());
            
            if (image == null) {
                throw new ClientException("Client not connected");
            }
            
            return image;
        } catch (IOException e) {
            throw new ClientException("Unable to capture frame", e);
        }
    }
    
    public byte[] getFrame(InputStream is) throws ClientException
    {
        try {
            // The first 32 bit int (4 bytes) of the stream will be the size of the image
            byte[] szBuf = new byte[4];
            if (is.read(szBuf, 0, 4) < 0) {
                return null;
            }
            int sz = Utils.byteArrayToInt(szBuf);
            
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
            
            if (streamStats != null) {
                streamStats.addFrame(bytesReadTotal);
            }
            
            return image;
        } catch (IOException e) {
            throw new ClientException("Unable to capture frame", e);
        }
    }
    
    public StreamStats getStreamStats()
    {
        return streamStats;
    }
}
