package com.jackpf.pirover.Controller;

import com.jackpf.pirover.Service.Utils;

import org.apache.commons.lang.ArrayUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

        byte[] buf = ArrayUtils.addAll(
            Utils.intToByteArray(controller.getAcceleration()),
            Utils.intToByteArray(controller.getSteering())
        );

        os.write(buf);
        
        os.flush();
    }
}
