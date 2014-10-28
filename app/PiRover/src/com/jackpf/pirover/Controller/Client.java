package com.jackpf.pirover.Controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.jackpf.pirover.Service.Utils;

public class Client
{
    private Socket socket;
    
    private Controller controller;
    
    public Client(String host, int port, Controller controller) throws ClientException
    {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new ClientException("Unable to connect to control server", e);
        }
        
        this.controller = controller;
    }
    
    public boolean isConnected()
    {
        return socket != null && socket.isConnected();
    }
    
    public void update() throws IOException
    {
        OutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(Utils.intToByteArray(controller.getAcceleratorPosition()));
        os.write(Utils.intToByteArray(controller.getSteeringPosition()));
        
        os.flush();
    }
}
