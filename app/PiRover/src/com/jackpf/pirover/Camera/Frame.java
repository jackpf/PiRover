package com.jackpf.pirover.Camera;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Frame
{
    private final byte[] image;
    
    public Frame(byte[] image)
    {
        this.image = image;
    }
    
    public byte[] getBytes()
    {
        return image;
    }
    
    @SuppressWarnings("deprecation")
    public Drawable getDrawable()
    {
        return new BitmapDrawable(
            BitmapFactory.decodeByteArray(image, 0, image.length)
        );
    }
}
