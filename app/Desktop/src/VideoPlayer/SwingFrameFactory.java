package VideoPlayer;
import com.jackpf.pirover.Camera.Frame;
import com.jackpf.pirover.Camera.FrameFactory;

public class SwingFrameFactory implements FrameFactory
{
    public Frame createFrame(byte[] image)
    {
        return new SwingFrame(image);
    }
}
