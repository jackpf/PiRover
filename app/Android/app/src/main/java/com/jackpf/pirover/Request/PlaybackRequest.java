package com.jackpf.pirover.Request;

import java.io.IOException;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.DrawableFrame;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class PlaybackRequest extends Request
{
    private BufferedVideo video;
    
    public PlaybackRequest(Object ...params)
    {
        super(params);
        
        video = (BufferedVideo) params[0];
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        if (!video.isLoaded()) {
            video.load();
        }
        
        RequestResponse response = new RequestResponse();
        
        DrawableFrame frame = (DrawableFrame) video.getFrame();
        
        if (frame != null) {
            response.put("drawable", frame.getDrawable());
            response.put("fps", video.getFps());
            response.put("current_frame", video.getFramePosition());
            response.put("frame_count", video.getFrameCount());
        } else {
            response.put("drawable", null);
        }

        return response;
    }
}