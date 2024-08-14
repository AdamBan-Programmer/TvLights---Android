package com.example.tvlights.Utils;

import android.graphics.Bitmap;

import java.io.Serializable;

public class BitmapModifier implements Serializable {

    public static Bitmap resizeBitmap(Bitmap bitmap, int reqHeightInPixels, int reqWidthInPixels)
    {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, reqWidthInPixels, reqHeightInPixels, true);
        return resized;
    }
}
