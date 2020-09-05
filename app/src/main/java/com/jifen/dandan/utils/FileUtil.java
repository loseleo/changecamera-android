package com.jifen.dandan.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import com.jifen.dandan.common.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gxj on 2018/2/8.
 */
public class FileUtil {
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "gxj";

    /**
     * 初始化保存路径
     *
     * @return
     */
    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param b 得到的图片
     */
    public static String saveBitmap(Bitmap b) {
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String imgPath = path + "/" + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(imgPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return imgPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    //读取图片旋转角度
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            LogUtils.e("zhangning","readPictureDegree : orientation = " + orientation);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                degree = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                degree = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //旋转图片
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotation = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        return rotation;
    }


}
