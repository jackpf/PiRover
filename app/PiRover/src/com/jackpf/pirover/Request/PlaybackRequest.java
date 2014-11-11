package com.jackpf.pirover.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.Frame;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class PlaybackRequest extends Request
{
    private static Client client;
    private InputStream is;
    
    public PlaybackRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        if (client == null || !client.isConnected()) {
            client = new Client();
        }
        
        if (is == null) {
            is = new FileInputStream(Environment.getExternalStorageDirectory() + "/PiRoverRecordings/record.pirover");
        }
        
        RequestResponse response = new RequestResponse();
        
        Frame image = client.getFrame(is);
        
        if (image.getBytes() != null) {
            response.put("drawable", image.getDrawable());
        } else {
            response.put("finished", true);
        }

        return response;
    }
}