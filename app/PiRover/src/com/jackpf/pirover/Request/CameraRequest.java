package com.jackpf.pirover.Request;

import java.io.IOException;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
        
        byte[] image = client.getFrame();

        Log.d("Camera", "Read " + image.length + " bytes");
        
        Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
        response.put("drawable", drawable);
        
        return response;
    }
}