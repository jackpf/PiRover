package com.jackpf.pirover.Camera;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableFrame extends Frame
{
    public DrawableFrame(byte[] image)
    {
        super(image);
    }
    
    @SuppressWarnings("deprecation")
    public Drawable getDrawable()
    {
        return new BitmapDrawable(
            BitmapFactory.decodeByteArray(image, 0, image.length)
        );
    }
}
