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
}
