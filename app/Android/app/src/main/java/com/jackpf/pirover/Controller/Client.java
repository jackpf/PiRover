package com.jackpf.pirover.Controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.jackpf.pirover.Service.Utils;

public class Client extends com.jackpf.pirover.Client.Client
{
    private Controller controller;
    
    public void connect(String host, int port, Controller controller) throws com.jackpf.pirover.Client.ClientException
    {
        super.connect(host, port);
        
        this.controller = controller;
    }
    
    public void update() throws IOException
    {
        OutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(Utils.intToByteArray(controller.getAcceleration()));
        os.write(Utils.intToByteArray(controller.getSteering()));
        
        os.flush();
    }
}
