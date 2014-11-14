package VideoPlayer;
import javax.swing.ImageIcon;

import com.jackpf.pirover.Camera.Frame;

public class SwingFrame extends Frame
{
    public SwingFrame(byte[] image)
    {
        super(image);
    }
    
    public ImageIcon getImage()
    {
        return new ImageIcon(image);
    }
}
