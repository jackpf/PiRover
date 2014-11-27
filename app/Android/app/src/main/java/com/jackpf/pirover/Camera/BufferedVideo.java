package com.jackpf.pirover.Camera;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BufferedVideo
{
    /**
     * Video frames
     */
    private List<Frame> frames = new ArrayList<Frame>();

    /**
     * Fps list
     */
    private List<Integer> fpsList = new ArrayList<Integer>();

    /**
     * Local client instance
     */
    private LocalClient client;

    /**
     * Filename
     */
    private String filename;

    /**
     * Current frame position
     */
    private int framePosition = -1;

    /**
     * Playback direction states
     */
    public static final int FASTFORWARD = 1, REWIND = -1, PLAY = 0;

    /**
     * Is playing state
     */
    private boolean isPlaying = false;

    /**
     * Direction state
     */
    private int direction = PLAY;

    /**
     * Fps calculator strategy
     */
    private FpsCalculator.Strategy fpsCalculator = FpsCalculator.DEFAULT_STRATEGY;

    /**
     * Constructor
     *
     * @param filename
     * @throws FileNotFoundException
     */
    public BufferedVideo(FrameFactory frameFactory, String filename) throws FileNotFoundException
    {
        client = new LocalClient(frameFactory, filename);
        this.filename = filename;
    }

    /**
     * Has video been loaded
     *
     * @return True if loaded, false if not
     */
    public boolean isLoaded()
    {
        return frames.size() > 0;
    }

    /**
     * Load video frames into memory
     *
     * @return
     */
    public BufferedVideo load()
    {
        Frame frame;

        do {
            try {
                frame = client.getFrame();

                if (frame != null) {
                    frames.add(frame);
                    fpsList.add(client.getFpsForLastFrame());
                }
            } catch (ClientException e) {
                frame = null;
            }
        } while (frame != null);

        isPlaying(true);

        return this;
    }

    /**
     * Resets video to original state
     */
    public void resetVideo()
    {
        isPlaying(false);
        setFramePosition(-1);
        setDirection(PLAY);
    }

    /**
     * Get next frame based on current video state
     * Also updates framePosition and frame states based on various factors
     *
     * @return
     */
    public Frame getFrame()
    {
        switch (direction) {
            case PLAY:
                framePosition++;
                break;
            case REWIND:
                framePosition = framePosition - 10 >= -1 ? framePosition - 10 : -1;
                break;
            case FASTFORWARD:
                framePosition = framePosition + 10 <= frames.size() ? framePosition + 10 : frames.size();
                break;
        }

        // Reached the end or the start of the video?
        if (framePosition < 0 || framePosition >= frames.size()) {
            isPlaying(false);
            return null;
        }

        return frames.get(framePosition);
    }

    /**
     * Set fps calculator strategy
     *
     * @param fpsCalculator
     */
    public void setFpsCalculator(FpsCalculator.Strategy fpsCalculator)
    {
        this.fpsCalculator = fpsCalculator;
    }

    /**
     * Get video fps
     *
     * @return
     */
    public int getFps()
    {
        return fpsCalculator.calculateFps(fpsList, framePosition);
    }

    /**
     * Get first frame in video
     * Will load the first frame if the video is not already loaded
     *
     * @return
     */
    public Frame getIcon()
    {
        // If the video is already loaded, just return the first frame
        if (isLoaded()) {
            return frames.get(0);
        }

        try {
            frames.add(client.getFrame());
            return frames.get(0);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get video filename
     *
     * @return
     */
    public String getName()
    {
        return this.filename;
    }

    /**
     * Set frame position
     *
     * @param framePosition
     */
    public void setFramePosition(int framePosition)
    {
        this.framePosition = framePosition;
    }

    /**
     * Get frame position
     *
     * @return
     */
    public int getFramePosition()
    {
        return framePosition;
    }

    /**
     * Get frame count
     *
     * @return
     */
    public int getFrameCount()
    {
        return frames.size();
    }

    /**
     * Set direction state
     *
     * @param direction
     */
    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    /**
     * Get direction state
     *
     * @return
     */
    public int getDirection()
    {
        return this.direction;
    }

    /**
     * Set video playing state
     *
     * @param isPlaying
     */
    public void isPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
    }

    /**
     * Toggle playing state
     */
    public void toggleIsPlaying()
    {
        isPlaying = !isPlaying;
    }

    /**
     * Get playing state
     *
     * @return
     */
    public boolean isPlaying()
    {
        return isPlaying;
    }
}
