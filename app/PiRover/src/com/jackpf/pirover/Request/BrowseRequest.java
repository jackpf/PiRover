package com.jackpf.pirover.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;

public class BrowseRequest extends Request
{
    public BrowseRequest(Object ...params)
    {
        super(params);
    }

    @Override
    public RequestResponse call(String ...args) throws ClientException, IOException
    {
        RequestResponse response = new RequestResponse();
        
        File dir = new File(args[0]);
        List<BufferedVideo> files = new ArrayList<BufferedVideo>();
        
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().substring(file.getName().lastIndexOf('.') + 1).equals(Recorder.RECORD_EXT)) {
                    try {
                        files.add(new BufferedVideo(file.getPath()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        response.put("videos", files);

        return response;
    }
}