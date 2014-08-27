package com.jackpf.pirover.Camera;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.jackpf.pirover.Service.Utils;

public class Client
{
    private Socket socket;
    private StreamStats streamStats;
    
    public Client()
    {
        // Connectionless client for local playback
    }
    
    public Client(String host, int port) throws ClientException
    {
        try {
            socket = new Socket(host, port);
            streamStats = new StreamStats();
        } catch (IOException e) {
            throw new ClientException("Unable to connect to camera server", e);
        }
    }
    
    public boolean isConnected()
    {
        return socket != null && socket.isConnected();
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
