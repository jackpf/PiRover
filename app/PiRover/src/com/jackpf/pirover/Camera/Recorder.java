package com.jackpf.pirover.Camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class Recorder
{
    private boolean recording;
    private static BufferedOutputStream out;
    
    public void toggleRecording()
    {
        recording = !recording;
    }
    
    public boolean isRecording()
    {
        return recording;
    }
    
    public void record(byte[] szHeader, byte[] image) throws IOException
    {
        if (!isRecording()) {
            return;
        }
        
        if (out == null) {
            File file = new File(Environment.getExternalStorageDirectory() + "/PiRoverRecordings/record.pirover");
            file.getParentFile().mkdir();
            file.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(file, true));
        }
        
        out.write(szHeader);
        out.write(image);
        
        out.flush();
    }
}
