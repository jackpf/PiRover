package com.jackpf.pirover.Request;

import java.io.IOException;

import com.jackpf.pirover.Controller.Client;
import com.jackpf.pirover.Controller.ClientException;
import com.jackpf.pirover.Controller.Controller;
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
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        String ip = args[0];
        
        if (ip == null) {
            throw new ClientException("No IP");
        }
        
        if (client == null || !client.isConnected()) {
            client = new Client(ip, 1338);
        }
        
        Controller controller = (Controller) params[0];
        
        client.update(controller);
        
        return null;
    }
}