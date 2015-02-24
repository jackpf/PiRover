package com.jackpf.pirover.Request;

import com.jackpf.pirover.Client.ClientException;
import com.jackpf.pirover.Controller.Client;
import com.jackpf.pirover.Controller.ControllerCommand;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

import java.io.IOException;

public class ShutdownRequest extends Request
{
    private static Client client;

    public ShutdownRequest(Object... params)
    {
        super(params);

        client = (Client) params[0];
    }

    @Override
    public RequestResponse call(Object ...args) throws ClientException, IOException
    {
        if (!client.isConnected()) {
            client.connect((String) args[0], (Integer) args[1]);
        }

        client.update(new ControllerCommand(0x2, new byte[]{}));
        
        return null;
    }
}