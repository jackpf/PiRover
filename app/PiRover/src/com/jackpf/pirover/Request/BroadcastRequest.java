package com.jackpf.pirover.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.jackpf.pirover.Broadcast.ConnectionException;
import com.jackpf.pirover.Broadcast.TimeoutException;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class BroadcastRequest extends Request
{
    public BroadcastRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call(String ...args) throws ConnectionException, TimeoutException, IOException
    {
        RequestResponse response = new RequestResponse();

        WifiManager wm = (WifiManager) params[0];
        ConnectivityManager cm = (ConnectivityManager) params[1];
        String ip = null, manualIp = (String) args[0];
        
        for (int retries = 0; ip == null && retries < 5; retries++) {
            if (!cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                throw new ConnectionException("Not connected to wifi");
            }
            
            byte[] handshake = stontba("PiRover");

            DatagramSocket socket = new DatagramSocket(null);
            socket.setBroadcast(true);
            
            String broadcastIp = "";
            
            if (manualIp == null) {
                @SuppressWarnings("deprecation") // formatIpAddress doesn't currently support IPv6 addresses
                String localIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                String[] parts = localIp.split("\\.");
                
                for (int i = 0; i < parts.length; i++) {
                    broadcastIp += i != parts.length - 1 ? parts[i] + "." : "255";
                }
            } else {
                broadcastIp = manualIp;
            }
            
            Log.d("Broadcast", "Attempting to broadcast to " + broadcastIp);
            
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
        
        if (ip == null) { // Tried max amount of times and still not determined IP address
            throw new TimeoutException("Maximum retries attempted");
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