package com.jackpf.pirover.Request;

import com.jackpf.pirover.Client.ClientException;
import com.jackpf.pirover.Controller.Client;
import com.jackpf.pirover.Controller.Controller;
import com.jackpf.pirover.Controller.ControllerCommand;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

import java.io.IOException;

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
    public RequestResponse call(Object ...args) throws ClientException, IOException
    {
        if (!client.isConnected()) {
            String ip = (String) args[0];
            Integer port = (Integer) args[1];

            if (ip == null) {
                throw new ClientException("No IP");
            }
            if (port == null) {
                throw new ClientException("No port");
            }

            client.connect(ip, port);
        }

        client.update((ControllerCommand) args[2]);
        
        return null;
    }
}