package com.jackpf.pirover.Request;

import java.io.IOException;

import com.jackpf.pirover.Control.Client;
import com.jackpf.pirover.Control.ClientException;
import com.jackpf.pirover.Control.Controller;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class ControlRequest extends Request
{
    private static Client client;
    
    public ControlRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call() throws ClientException, IOException
    {
        if (client == null || !client.isConnected()) {
            client = new Client("192.168.0.8", 1338);
        }
        
        Controller controller = (Controller) params[0];
        
        client.update(controller);
        
        return null;
    }
}