package com.jackpf.pirover.Request;

import java.io.IOException;

import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.Frame;
import com.jackpf.pirover.Camera.Video;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class PlaybackRequest extends Request
{
    private /*static*/ Video video;
    
    public PlaybackRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        if (video == null) {
            video = new Video(args[0])
                .load();
        }
        
        RequestResponse response = new RequestResponse();
        
        Frame frame = video.getFrame();
        
        if (frame != null) {
            response.put("drawable", frame.getDrawable());
            response.put("fps", 11); // TODO: Needs to be calculated
            response.put("current_frame", video.getFramePosition());
            response.put("frame_count", video.getFrameCount());
        } else {
            response.put("drawable", null);
        }

        return response;
    }
}