package com.jackpf.pirover.Request;

import java.io.IOException;

import android.os.Environment;

import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.Frame;
import com.jackpf.pirover.Camera.Video;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class PlaybackRequest extends Request
{
    private static Video video;
    
    public PlaybackRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        if (video == null) {
            video = new Video(Environment.getExternalStorageDirectory() + "/PiRoverRecordings/record.pirover")
                .load();
        }
        
        RequestResponse response = new RequestResponse();
        
        Frame frame = video.getFrame();
        
        if (frame != null) {
            response.put("drawable", frame.getDrawable());
            response.put("fps", 11); // TODO: Needs to be calculated
        } else {
            response.put("drawable", null);
        }

        return response;
    }
}