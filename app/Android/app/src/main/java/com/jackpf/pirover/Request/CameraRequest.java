package com.jackpf.pirover.Request;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Camera.DrawableFrame;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Client.ClientException;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

import java.io.IOException;

public class CameraRequest extends Request
{
    private static Client client;
    private Recorder recorder;
    
    public CameraRequest(Object ...params)
    {
        super(params);
        
        client = (Client) params[0];
        recorder = (Recorder) params[1];
    }

    @Override
    public RequestResponse call(Object ...args) throws ClientException, IOException
    {
        if (!client.isConnected()) {
            String ip = (String) args[0];
            Integer port = (Integer) args[1];

            if (ip == null || port == null) {
                throw new ClientException("No IP/Port");
            }

            client.connect(ip, port);
        }
        
        RequestResponse response = new RequestResponse();
        
        DrawableFrame image = (DrawableFrame) client.getFrame();
        
        if (recorder.isRecording()) {
            recorder.recordFrame(image);
        }
        
        response.put("drawable", image.getDrawable());
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