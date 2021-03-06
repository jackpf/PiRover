package com.jackpf.pirover.Request;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.DrawableFrame;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

import java.io.IOException;

public class PlaybackRequest extends Request
{
    private BufferedVideo video;
    
    public PlaybackRequest(Object ...params)
    {
        super(params);
        
        video = (BufferedVideo) params[0];
    }

    @Override
    public RequestResponse call(Object ...args) throws ClientException, IOException
    {
        if (!video.isLoaded()) {
            video.load().isPlaying(true);
        }
        
        RequestResponse response = new RequestResponse();
        
        DrawableFrame frame = (DrawableFrame) video.getFrame();
        
        response.put("drawable", frame != null ? frame.getDrawable() : null);
        response.put("fps", frame != null ? video.getFps() : 0);
        response.put("current_frame", video.getFramePosition());
        response.put("frame_count", video.getFrameCount());
        response.put("info", video.getInfo());

        return response;
    }
}