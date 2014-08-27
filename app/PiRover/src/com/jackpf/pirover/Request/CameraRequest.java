package com.jackpf.pirover.Request;

import java.io.IOException;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Service.Utils;

public class CameraRequest extends Request
{
    private static Client client;
    private Recorder recorder;
    
    public CameraRequest(Object ...params)
    {
        super(params);
        
        recorder = new Recorder();
    }

    @Override
    public RequestResponse call() throws ClientException, IOException
    {
        if (client == null || !client.isConnected()) {
            client = new Client("192.168.0.8", 1337);
        }
        
        RequestResponse response = new RequestResponse();
        
        byte[] image = client.getFrame();

        Log.d("Camera", "Read " + image.length + " bytes");
        
        if (recorder.isRecording()) {
            recorder.record(Utils.intToByteArray(image.length), image);
        }
        
        @SuppressWarnings("deprecation")
        Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
        response.put("drawable", drawable);

        response.put("fpsCount", client.getStreamStats().getFps());
        response.put("bandwidth", client.getStreamStats().getBandwidth());
        response.put("recording", recorder.isRecording());
        
        return response;
    }
    
    public Recorder getRecorder()
    {
        return recorder;
    }
}