package com.jackpf.pirover.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.ClientException;
import com.jackpf.pirover.Camera.DrawableFrameFactory;
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
            File[] filesList = dir.listFiles();

            Arrays.sort(filesList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return -Long.compare(f1.lastModified(), f2.lastModified());
                }
            });
            
            for (File file : filesList) {
                if (file.isFile() && file.getName().substring(file.getName().lastIndexOf('.') + 1).equals(Recorder.RECORD_EXT)) {
                    try {
                        files.add(new BufferedVideo(new DrawableFrameFactory(), file.getPath()));
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