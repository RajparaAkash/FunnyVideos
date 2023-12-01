package com.exmple.funnyvideo.Tools;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    private static String getName(String str, int i) {
        return str + "__" + i;
    }

    public static File getCachedFile(File file, String str, int i) {
        return new File(file, getName(str, i));
    }

    public static void saveBitmapToFile(File file, Bitmap bitmap) {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bufferedOutputStream);
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
