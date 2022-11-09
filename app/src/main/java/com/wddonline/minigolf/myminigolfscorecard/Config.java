package com.wddonline.minigolf.myminigolfscorecard;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mobile World on 5/31/2018.
 */
public class Config {
    public static String userType = "";
//    public static String root_link = "http://minigolf.wddonline.com/";
    public static String root_link = "https://www.myminigolfscorecard.com";
    public static Typeface mainTypeFace;
    public static Typeface ziptyTypeFace;
    public static LocationData locationData;
    public static LocationData parseData;
    public static CourseData courseData;
    public static int selectedScore;
    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("TAG", "Error getting bitmap", e);
        }
        return bm;
    }
}
