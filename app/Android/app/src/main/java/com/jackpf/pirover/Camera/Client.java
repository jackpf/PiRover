package com.jackpf.pirover.Camera;

import com.jackpf.pirover.Service.Utils;

import java.io.IOException;
import java.io.InputStream;

public class Client extends com.jackpf.pirover.Client.Client
{
    private StreamStats streamStats;
    private FrameFactory frameFactory;

    public Client(FrameFactory frameFactory, StreamStats streamStats)
    {
        this.frameFactory = frameFactory;
        this.streamStats = streamStats;
    }

    public void connect(String host, int port) throws com.jackpf.pirover.Client.ClientException
    {
        super.connect(host, port);
    }

    public Frame getFrame() throws ClientException
    {
        if (!isConnected()) {
            throw new ClientException("Client not connected");
        }

        try {
            Frame frame = consumeFrameFromStream(socket.getInputStream());

            if (frame == null) {
                throw new ClientException("Client not connected");
            }

            return frame;
        } catch (IOException e) {
            throw new ClientException("Unable to capture frame", e);
        }
    }

    protected Frame consumeFrameFromStream(InputStream is) throws ClientException
    {
        try {
            // The first 32 bit int (4 bytes) of the stream will be the size of the image
            byte[] szBuf = new byte[4];
            if (is.read(szBuf, 0, 4) < 0) {
                return null;
            }
            int sz = Utils.byteArrayToInt(szBuf);

            if (sz < 0) {
                throw new IOException("Invalid frame buffer size: " + sz);
            }

            // Sometimes this seems to cause an out of memory exception when something else goes wrong like a disconnect
            // not entirely sure why...
            byte[] buffer = new byte[sz], image = new byte[sz];

            int bytesRead = 0, bytesReadTotal = 0, bytesWritten = 0;

            // Read the image data
            while (bytesReadTotal < sz) {
                bytesRead = is.read(buffer, 0, sz - bytesReadTotal);
                bytesReadTotal += bytesRead;

                for (int i = 0; i < bytesRead; i++, bytesWritten++) {
                    image[bytesWritten] = buffer[i];
                }
            }

            if (streamStats != null) {
                streamStats.addFrame(bytesReadTotal);
            }

            return frameFactory.createFrame(image);
        } catch (IOException e) {
            throw new ClientException("Unable to capture frame", e);
        } catch (OutOfMemoryError e) {
            throw new ClientException("Unable to allocate memory for frame");
        }
    }

    public StreamStats getStreamStats()
    {
        return streamStats;
    }
}
