package VideoPlayer;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.Frame;

public class VideoPlayer extends JFrame
{
    public VideoPlayer()
    {
       setTitle("PiRover Video Player");
       setSize(320, 240);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setLayout(new BorderLayout());
    }
    
    public void play(String filename) throws FileNotFoundException
    {
       final BufferedVideo video = new BufferedVideo(new SwingFrameFactory(), filename)
           .load();
       
       final JLabel label = new JLabel();
       add(label);
       
       new Thread(new Runnable() {
           public void run() {
               Frame frame;
               
               do {
                   frame = video.getFrame();
                   
                   label.setIcon(((SwingFrame) frame).getImage());
                   repaint();
                   try {
                       Thread.sleep((int) Math.round(1000.0 / 11.0));
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               } while (frame != null);
           }
       }).start();
    }
    
    public static void main(String[] args)
    {
        if (args.length == 0) {
            System.err.println("No file to play");
            return;
        }
        
        VideoPlayer t = new VideoPlayer();
        t.setVisible(true);

        try { 
            t.play(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}