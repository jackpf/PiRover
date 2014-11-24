package VideoPlayer;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Camera.Frame;

public class VideoPlayer extends JFrame
{
    private final String TITLE = "PiRover Video Player";
    private final int WINDOW_X = 320, WINDOW_Y = 240;
    private PlaybackThread playbackThread;
    
    public VideoPlayer()
    {
       setTitle(TITLE);
       setSize(WINDOW_X, WINDOW_Y);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setLayout(new BorderLayout());
       
       addComponentListener(new ComponentListener() {
           public void componentResized(ComponentEvent e) {
               Rectangle r = getBounds();
               if (playbackThread != null) {
                   playbackThread.setWindowSize((int) Math.round(r.getWidth()), (int) Math.round(r.getHeight()));
               }
           }
           public void componentHidden(ComponentEvent e) { }
           public void componentShown(ComponentEvent e) { }
           public void componentMoved(ComponentEvent e) { }
       });
    }
    
    public void play(String filename) throws FileNotFoundException
    {
        String[] parts = filename.split("/");
        setTitle(TITLE + " - " + parts[parts.length - 1]);
        
        final BufferedVideo video = new BufferedVideo(new SwingFrameFactory(), filename)
           .load();
       
        final JLabel label = new JLabel();
        add(label);
       
        playbackThread = new PlaybackThread(video, label);
        new Thread(playbackThread).start();
    }
    
    private class PlaybackThread implements Runnable
    {
        private BufferedVideo video;
        private JLabel label;
        private int windowX = WINDOW_X, windowY = WINDOW_Y;
        
        public PlaybackThread(BufferedVideo video, JLabel label)
        {
            this.video = video;
            this.label = label;
        }
        
        public void setWindowSize(int windowX, int windowY)
        {
            this.windowX = windowX;
            this.windowY = windowY;
        }
        
        public void run() {
            Frame frame;
            
            do {
                frame = video.getFrame();
                
                ImageIcon imageIcon = ((SwingFrame) frame).getImage();
                Image image = imageIcon.getImage().getScaledInstance(windowX, windowY, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(image);
                
                label.setIcon(imageIcon);
                
                repaint();
                
                try {
                    Thread.sleep((int) Math.round(1000.0 / video.getFps()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (frame != null);
        }
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