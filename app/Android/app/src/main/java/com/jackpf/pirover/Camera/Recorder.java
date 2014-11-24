package com.jackpf.pirover.Camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Environment;

import com.jackpf.pirover.Service.Utils;

public class Recorder
{
    private boolean recording;
    private static BufferedOutputStream out;
    private StreamStats streamStats;
    
    public static final String RECORD_DIR = "PiRoverRecordings", RECORD_EXT = "prr";
    
    public Recorder(StreamStats streamStats)
    {
        this.streamStats = streamStats;
    }
    
    private BufferedOutputStream createStream() throws IOException
    {
        String filename = String.format(
            "%s/%s/%s.%s",
            Environment.getExternalStorageDirectory(),
            RECORD_DIR,
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()),
            RECORD_EXT
        );
        
        File file = new File(filename);
        
        file.getParentFile().mkdir();
        file.createNewFile();
        
        return new BufferedOutputStream(new FileOutputStream(file, true));
    }
    
    public void toggleRecording()
    {
        recording = !recording;
        
        try {
            if (recording) {
                out = createStream(); // Create a new file to record to
            } else {
                out.close(); // Close file we're recording to
            }
        } catch (IOException e) {
            e.printStackTrace();
            
            recording = !recording; // Toggle again since we weren't successful!
        }
    }
    
    public boolean isRecording()
    {
        return recording;
    }
    
    public void recordFrame(Frame frame) throws IOException
    {
        if (!isRecording() || out == null) {
            throw new IOException("No stream to record to");
        }
        
        /**
         * Prr header format:
         *  @param 1 byte:  The fps of the stream at current frame
         *  @param 1 byte:  Frame size
         *  @param x bytes: Image data
         */
        out.write(Utils.intToByteArray((int) Math.round(streamStats.getFps())));
        out.write(Utils.intToByteArray(frame.getBytes().length));
        out.write(frame.getBytes());
        
        out.flush();
    }
}
