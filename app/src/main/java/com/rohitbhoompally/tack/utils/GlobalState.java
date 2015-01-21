package com.rohitbhoompally.tack.utils;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Rohit on 1/11/15.
 */
public class GlobalState extends Application {
    private static ArrayList<Bitmap> clickedBitmaps = new ArrayList<>();

    public static void addBitmap(Bitmap b) {
        clickedBitmaps.add(b);
    }

    public static void addBitmapAtPosition(Bitmap b, int position) {
        clickedBitmaps.add(position, b);
    }

    public static void clearBitmaps(){
        clickedBitmaps.clear();
    }

    public static ArrayList<Bitmap> getClickedBitmaps() {
        return clickedBitmaps;
    }
}
