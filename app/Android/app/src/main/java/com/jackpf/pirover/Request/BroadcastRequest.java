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

import com.jackpf.pirover.Broadcast.BroadcastResolver;
import com.jackpf.pirover.Broadcast.ConnectionException;
import com.jackpf.pirover.Broadcast.TimeoutException;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Service.Utils;

public class BroadcastRequest extends Request
{
    private WifiManager         wm;
    private ConnectivityManager cm;
    private BroadcastResolver   broadcastResolver;
    
    public BroadcastRequest(Object ...params)
    {
        super(params);
        
        wm                  = (WifiManager) params[0];
        cm                  = (ConnectivityManager) params[1];
        broadcastResolver   = new BroadcastResolver();
    }

    @Override
    public RequestResponse call(String ...args) throws ConnectionException, TimeoutException, IOException
    {
        RequestResponse response = new RequestResponse();

        int port = Integer.parseInt(args[0]);
        String manualIp = args.length > 1 ? args[1] : null;
        
        for (int retries = 0; !broadcastResolver.packetIsValid() && retries < 5; retries++) {
            if (!cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                throw new ConnectionException("Not connected to wifi");
            }
            
            byte[] handshake = Utils.stringToNullTerminatedByteArray("PiRover");

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
            DatagramPacket packet = new DatagramPacket(handshake, handshake.length, host, port);
            socket.send(packet);
            
            socket.setSoTimeout(1000);
            
            try {
                byte[] buf = new byte[64];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                broadcastResolver.setPacket(packet);
            } catch (SocketTimeoutException e) {
                // Retry...
            }
            
            socket.close();
        }
        
        if (broadcastResolver.resolveIp() == null) { // Tried max amount of times and still not determined IP address
            throw new TimeoutException("Maximum retries attempted");
        }
        
        response.put("ip", broadcastResolver.resolveIp());
        response.put("ports", broadcastResolver.resolvePorts());
        
        return response;
    }
}