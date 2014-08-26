package com.jackpf.pirover.Camera;

import java.io.IOException;
import java.net.Socket;

public class Client
{
    private Socket sock;
    
    public Client(String host, int port) throws IOException
    {
        sock = new Socket(host, port);
    }
    
    public Socket getSocket()
    {
        return sock;
    }
}
