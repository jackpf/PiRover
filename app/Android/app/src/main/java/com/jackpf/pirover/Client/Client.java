package com.jackpf.pirover.Client;

import java.io.IOException;
import java.net.Socket;

public abstract class Client
{
    protected Socket socket;
    
    public void connect(String host, Integer port) throws ClientException
    {
        // Just a sanity check in case port resolution failed or something
        if (host == null || port == null) {
            throw new ClientException("No IP/Port");
        }

        try {
            if (isConnected()) {
                disconnect();
            }

            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new ClientException("Unable to connect to server", e);
        }
    }
    
    public boolean isConnected()
    {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    
    public void disconnect()
    {
        try {
            if (isConnected()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
