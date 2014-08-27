package com.jackpf.pirover.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class PlaybackRequest extends Request
{
    private static Client client;
    private Recorder recorder;
    private InputStream is;
    
    public PlaybackRequest(Object ...params)
    {
        super(params);
        
        recorder = new Recorder();
    }

    @Override
    public RequestResponse call() throws ClientException, IOException
    {
        if (client == null || !client.isConnected()) {
            client = new Client();
        }
        
        if (is == null) {
            is = new FileInputStream(Environment.getExternalStorageDirectory() + "/PiRoverRecordings/record.pirover");
        }
        
        RequestResponse response = new RequestResponse();
        
        byte[] image = client.getFrame(is);
        
        if (image != null) {
            Log.d("Camera", "Read " + image.length + " bytes");
            
            Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
            response.put("drawable", drawable);
        } else {
            response.put("finished", true);
        }

        return response;
    }
    
    public Recorder getRecorder()
    {
        return recorder;
    }
}