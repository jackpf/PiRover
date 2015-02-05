package com.jackpf.pirover.Controller;

import com.jackpf.pirover.Service.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Client extends com.jackpf.pirover.Client.Client
{
    public void update(ControllerCommand cmd) throws IOException
    {
        OutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(Utils.intToByteArray(cmd.id));
        os.write(cmd.bytes);
        
        os.flush();
    }
}
