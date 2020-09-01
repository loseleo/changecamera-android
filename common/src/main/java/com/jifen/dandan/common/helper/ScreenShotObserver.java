package com.jifen.dandan.common.helper;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ScreenShotObserver {

    private Callback mCallback;
    private Context mContext;
    private final List<String> historyList = new ArrayList<>();
    private static final ExecutorService mExecutorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "screen_shot_check_thread");
        }
    });
    private final List<CheckFileExitTask> checkFileExitTasks = new ArrayList<>();

    public ScreenShotObserver(Context context) {
        this.mContext = context.getApplicationContext();
    }


    private ContentObserver observer = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Cursor cursor = null;
            try {
                cursor = mContext.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media.DATA},
                        null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
                if (cursor == null) {
                    return;
                }
                if (cursor.moveToNext()) {
                    String imageFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String imageFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (isScreenShot(imageFileName) || !historyList.contains(imageFileName)) {
                        historyList.add(imageFileName);
                        if (mCallback != null) {
                            mCallback.onReceiveScreenShot(imageFilePath);
                        }
                        CheckFileExitTask task = new CheckFileExitTask(imageFilePath);
                        checkFileExitTasks.add(task);
                        mExecutorService.submit(task);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    };

    public void resume() {

        try {
            mContext.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    true,
                    observer);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void pause() {

        try {
            mContext.getContentResolver().unregisterContentObserver(observer);
            for (CheckFileExitTask checkFileExitTask : checkFileExitTasks) {
                checkFileExitTask.stop();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public interface Callback {

        void onReceiveScreenShot(String filePath);

        void onGiveUpScreenShot(String filePath);

        void onScreenShotFileReady(String filePath);
    }

    public ScreenShotObserver setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }


    private static boolean isScreenShot(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        fileName = fileName.toLowerCase();
        return fileName.contains("screenshot")
                || fileName.contains("screen") && fileName.contains("shot")
                || fileName.contains("截屏");
    }


    private class CheckFileExitTask implements Runnable {

        private final long duration = 1000;
        private final int tryTimes = 5;
        private int currentTry = 0;
        private File targetFile;
        private String filePath;
        private final AtomicBoolean isSop = new AtomicBoolean(false);

        public CheckFileExitTask(String filePath) {
            targetFile = new File(filePath);
            this.filePath = filePath;
        }

        @Override
        public void run() {

            for (; currentTry < tryTimes && !isSop.get(); currentTry++) {

                if (targetFile.exists()) {
                    if (mCallback != null) {
                        mCallback.onScreenShotFileReady(filePath);
                    }
                    return;
                }
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (isSop.get()) {
                return;
            }
            if (mCallback != null) {
                mCallback.onGiveUpScreenShot(filePath);
            }
        }

        public void stop() {
            isSop.set(true);
        }
    }
}
