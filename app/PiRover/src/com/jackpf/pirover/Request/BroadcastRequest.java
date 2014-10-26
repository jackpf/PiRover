package com.jackpf.pirover.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.jackpf.pirover.Controller.ClientException;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class BroadcastRequest extends Request
{
    public BroadcastRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        RequestResponse response = new RequestResponse();
        
        String ip = null;
        
        while (ip == null) {
            byte[] handshake = stontba("PiRover");

            DatagramSocket socket = new DatagramSocket(null);
            socket.setBroadcast(true);
            
            WifiManager wm = (WifiManager) params[0];
            
            @SuppressWarnings("deprecation") // formatIpAddress doesn't currently support IPv6 addresses
            String broadcastIp = "", localIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            String[] parts = localIp.split("\\.");
            
            for (int i = 0; i < parts.length; i++) {
                broadcastIp += i != parts.length - 1 ? parts[i] + "." : "255";
            }
            
            InetAddress host = InetAddress.getByName(broadcastIp);
            DatagramPacket packet = new DatagramPacket(handshake, handshake.length, host, 2080);
            socket.send(packet);
            
            socket.setSoTimeout(1000);
            
            try {
                socket.receive(packet);
                ip = packet.getAddress().getHostAddress();
            } catch (SocketTimeoutException e) {
                ip = null;
            }
            
            socket.close();
        }
        
        response.put("ip", ip);
        
        return response;
    }
    
    protected byte[] stontba(String s)
    {
        byte[] bytes = new byte[s.length() + 1];
        
        for (int i = 0; i < s.length(); i++) {
            bytes[i] = (byte) s.charAt(i);
        }
        
        bytes[s.length()] = '\0';
        
        return bytes;
    }
}