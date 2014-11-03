package com.jackpf.pirover.Request;

import java.io.IOException;

import com.jackpf.pirover.Client.ClientException;
import com.jackpf.pirover.Controller.Client;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class ControlRequest extends Request
{
    private static Client client;
    private static Controller controller;
    
    public ControlRequest(Object ...params)
    {
        super(params);

        client = (Client) params[0];
        controller = (Controller) params[1];
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        String ip = args[0], portStr = args[1];
        
        if (ip == null) {
            throw new ClientException("No IP");
        }
        if (portStr == null) {
            throw new ClientException("No port");
        }
        
        int port = Integer.parseInt(portStr);
        
        if (!client.isConnected()) {
            client.connect(ip, port, controller);
        }
        
        client.update();
        
        return null;
    }
}