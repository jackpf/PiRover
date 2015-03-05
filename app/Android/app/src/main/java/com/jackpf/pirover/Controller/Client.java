package com.jackpf.pirover.Controller;

import com.jackpf.pirover.Service.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public byte[] retrieve(int len) throws IOException
    {
        byte[] data = new byte[len];

        InputStream is = new DataInputStream(socket.getInputStream());

        is.read(data, 0, len);

        return data;
    }
}
