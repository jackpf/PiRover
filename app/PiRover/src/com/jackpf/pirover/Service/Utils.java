package com.jackpf.pirover.Service;

public class Utils
{
    public static int byteArrayToInt(byte[] bytes)
    {
        int int32 = 0;
        
        for (int i = 0; i < bytes.length; i++) {
            int32 += (bytes[i] & 0xFF) << (8 * i);
        }
        
        return int32;
    }
    
    public static byte[] intToByteArray(int i)
    {
        return new byte[]{
            (byte) (i),
            (byte) (i >> 8),
            (byte) (i >> 16),
            (byte) (i >> 24)
        };  
    }
    
    public static byte[] stringToNullTerminatedByteArray(String s)
    {
        byte[] bytes = new byte[s.length() + 1];
        
        for (int i = 0; i < s.length(); i++) {
            bytes[i] = (byte) s.charAt(i);
        }
        
        bytes[s.length()] = '\0';
        
        return bytes;
    }
}
