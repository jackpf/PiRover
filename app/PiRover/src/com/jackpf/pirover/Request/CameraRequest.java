package com.jackpf.pirover.Request;

import java.io.IOException;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Entity.RequestResponse;
import com.jackpf.pirover.Model.RequestInterface;

public class CameraRequest extends RequestInterface
{
    private static Client client;
    private static Recorder recorder;
    
    public CameraRequest(Object ...params)
    {
        super(params);
        
        recorder = new Recorder();
    }

    @Override
    public RequestResponse call() throws IOException
    {
        if (client == null) {
            client = new Client("192.168.0.8", 1337);
        }
        
        RequestResponse response = new RequestResponse();
        
        byte[] image = client.getFrame();

        //Log.d("Camera", "Read " + image.length + " bytes");
        
        if (recorder.isRecording()) {
            recorder.record(client.intToByteArray(image.length), image);
        }
        
        Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
        response.put("drawable", drawable);

        response.put("fpsCount", client.getStreamStats().getFps());
        response.put("bandwidth", client.getStreamStats().getBandwidth());
        
        return response;
    }
    
    public Recorder getRecorder()
    {
        return recorder;
    }
}