package com.jackpf.pirover.Camera;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FpsCalculator
{
    /**
     * Default strategy to use if none is set
     */
    public static final Strategy DEFAULT_STRATEGY = new ReplicateStrategy();

    /**
     * Fps calculator strategy interface
     */
    public static interface Strategy
    {
        /**
         * Calculate fps
         *
         * @param fpsList       A list of fps values relating to each frame
         * @param framePosition Position of the currently played frame
         * @return              Fps to play the buffered video at
         */
        public int calculateFps(List<Integer> fpsList, int framePosition);
    }

    /**
     * Replicate strategy
     * Uses the fps that the stream was recorded at
     * Gives an accurate representation of the recorded data for a stream
     * However dropped frames will cause the video to lag substantially
     */
    public static class ReplicateStrategy implements Strategy
    {
        @Override
        public int calculateFps(List<Integer> fpsList, int framePosition)
        {
            return fpsList.get(framePosition);
        }
    }

    /**
     * Mean strategy
     * Uses the mean value of all fps values
     * This gives a constant fps but will not play the video at the correct speed
     */
    public static class SmoothMeanStrategy implements Strategy
    {
        private Integer smoothFps;

        @Override
        public int calculateFps(List<Integer> fpsList, int framePosition)
        {
            if (smoothFps == null) {
                int sum = 0;

                for (int fps : fpsList) {
                    sum += fps;
                }

                smoothFps = Math.round(sum / fpsList.size());
            }

            return smoothFps;
        }
    }

    /**
     * Median strategy
     * This will give a constant fps, and ignore outliers by returning the median value
     */
    public static class SmoothMedianStrategy implements Strategy
    {
        private Integer smoothFps;

        @Override
        public int calculateFps(List<Integer> fpsList, int framePosition)
        {
            if (smoothFps == null) {
                Collections.sort(fpsList);
                smoothFps = fpsList.get(fpsList.size() / 2);
            }

            return smoothFps;
        }
    }

    /**
     * Modal strategy
     * This will give a constant fps, although may calculate the proper value wrong if many frames are lost
     */
    public static class SmoothModalStrategy implements Strategy
    {
        private Integer smoothFps;

        @Override
        public int calculateFps(List<Integer> fpsList, int framePosition)
        {
            if (smoothFps == null) {
                Map<Integer, Integer> fpsFrequency = new Hashtable<Integer, Integer>();

                for (int fps : fpsList) {
                    Integer freq = fpsFrequency.get(fps);
                    fpsFrequency.put(fps, freq != null ? freq + 1 : 1);
                }

                int modalFps = -1, modalFpsFrequency = 0;
                for (Map.Entry<Integer, Integer> frequency : fpsFrequency.entrySet()) {
                    int fps = frequency.getKey(), freq = frequency.getValue();

                    if (freq > modalFpsFrequency || (freq == modalFpsFrequency && fps > modalFps)) {
                        modalFps = frequency.getKey();
                        modalFpsFrequency = frequency.getValue();
                    }
                }

                smoothFps = modalFps;
            }

            return smoothFps;
        }
    }

    /**
     * Specify strategy
     * Play all videos at a user specified fps
     */
    public static class SpecifyStrategy implements Strategy
    {
        private final int fps;

        public SpecifyStrategy(int fps)
        {
            this.fps = fps;
        }

        @Override
        public int calculateFps(List<Integer> fpsList, int framePosition)
        {
            return fps;
        }
    }
}
