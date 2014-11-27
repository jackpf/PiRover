package com.jackpf.pirover.Request;

import java.io.IOException;

import com.jackpf.pirover.Camera.Client;
import com.jackpf.pirover.Camera.DrawableFrame;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Client.ClientException;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

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
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        if (!client.isConnected()) {
            String ip = args[0], portStr = args[1];

            if (ip == null) {
                throw new ClientException("No IP");
            }
            if (portStr == null) {
                throw new ClientException("No port");
            }

            int port = Integer.parseInt(portStr);

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