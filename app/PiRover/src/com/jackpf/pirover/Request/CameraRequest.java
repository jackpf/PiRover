package com.jackpf.pirover.Request;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Entity.RequestResponse;
import com.jackpf.pirover.Model.RequestInterface;

public class CameraRequest extends RequestInterface
{
    static Client client;
    
    public CameraRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call() throws IOException
    {
        if (client == null) {
            client = new Client("192.168.0.8", 1337);
        }
        
        RequestResponse response = new RequestResponse();
        Socket sock = client.getSocket();
        OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream());
        out.write("frame", 0, 5);
        out.flush();
        
        InputStream is = client.getSocket().getInputStream();

        // The first int (4 bytes) of the stream will be the size of the image
        byte[] szBuf = new byte[4];
        is.read(szBuf, 0, 4);
        int sz = byteArrayToInt(szBuf);
        
        Log.d("d", "Receiving " + sz + " bytes");
        
        byte[] buffer = new byte[sz], image = new byte[sz];
        int bytesRead = 0, bytesReadTotal = 0, bytesWritten = 0;
        
        while (bytesReadTotal < sz) {
            bytesRead = is.read(buffer, 0, sz - bytesReadTotal);
            bytesReadTotal += bytesRead;
            
            Log.d("d", "Read " + bytesRead + " bytes");
            
            for (int i = 0; i < bytesRead; i++, bytesWritten++) {
                image[bytesWritten] = buffer[i];
            }
        }

        Log.d("d", "Read " + bytesReadTotal + " bytes total");
        Log.d("d", "Buffered " + bytesWritten + " bytes");
        
        Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
        response.put("drawable", drawable);
        
        return response;
    }
    
    protected int byteArrayToInt(byte[] bytes)
    {
        int int32 = 0;
        
        for (int i = 0; i < bytes.length; i++) {
            int32 += (bytes[i] & 0xFF) << (8 * i);
        }
        
        return int32;
    }
}