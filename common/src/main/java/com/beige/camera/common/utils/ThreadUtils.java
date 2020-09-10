package com.beige.camera.common.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void runOnUIThread(Runnable runnable) {

        if (checkMainThread()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    public static Handler getMainThreadHandler() {
        return handler;
    }

    /**
     * 检查主线程
     *
     * @return true 主线程  false no
     */
    public static boolean checkMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 检查主线程，不是主线程将抛出异常
     *
     * @throws Exception
     */
    public static void checkMainThreadOrThrow() throws RuntimeException {
        if (!checkMainThread()) {
            throw new RuntimeException("require main thread,find thread : " + Thread.currentThread().getName());
        }
    }
}
