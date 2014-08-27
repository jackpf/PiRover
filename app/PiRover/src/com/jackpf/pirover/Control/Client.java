package com.jackpf.pirover.Control;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.jackpf.pirover.Service.Utils;

public class Client
{
    private Socket socket;
    
    public Client(String host, int port) throws ClientException
    {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new ClientException("Unable to connect to control server", e);
        }
    }
    
    public boolean isConnected()
    {
        return socket != null && socket.isConnected();
    }
    
    public void update(Controller controller) throws IOException
    {
        OutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(Utils.intToByteArray(controller.getAcceleratorPosition()));
        os.write(Utils.intToByteArray(controller.getSteeringPosition()));
        
        os.flush();
    }
}
