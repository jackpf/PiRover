package com.jackpf.pirover.Broadcast;

import java.net.DatagramPacket;
import java.util.HashMap;

public class BroadcastResolver
{
    private String ip;
    private String data;
    
    public static class PortsMap extends HashMap<String, String> {
        private static final long serialVersionUID = -133788573345576255L;
    }
    
    public void setPacket(DatagramPacket packet)
    {
        if (packet != null) {
            ip = packet.getAddress().getHostAddress();
            data = new String(packet.getData(), 0, packet.getLength() - 1 /* Null terminated */);
        }
    }
    
    public boolean packetIsValid()
    {
        if (data == null) {
            return false;
        }
        
        String[] parts = data.split(";");
        
        if (parts.length > 0) {
            if (parts[0].equals("PiRover")) {
                return true;
            }
        }
        
        return false;
    }
    
    public String resolveIp()
    {
        return ip;
    }
    
    public PortsMap resolvePorts()
    {
        if (data == null) {
            return null;
        }

        PortsMap ports = new PortsMap();
        
        for (String part : data.split(";")) {
            String[] parts = part.split(":");
            
            if (parts.length > 1) {
                ports.put(parts[0], parts[1]);
            }
        }
        
        return ports;
    }
}
