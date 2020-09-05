package com.jifen.dandan.ringtone.manager;

import android.content.ContentValues;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.utils.AppUtils;
import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.ringtone.dialog.RingPermissionDialog;
import com.jifen.dandan.ringtone.permission.PermissionUtils;

import java.io.File;


public class RingToneManager {

    private RingToneManager() {
    }

    private Context mContext;
    private RingtoneManager mRingtoneManager;
    private Ringtone mRingtone;

    private static class RingToneManagerHolder {
        private static final RingToneManager INSTANCE = new RingToneManager();
    }

    public static RingToneManager getInstance() {
        return RingToneManagerHolder.INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
//        mRingtoneManager = new RingtoneManager(context.getApplicationContext());
//        mRingtoneManager.setStopPreviousRingtone(true);
    }


    /**
     * 设置新的铃声
     *
     * @param path
     */
    public void setRingtoneImpl(String path) {
        File ringtoneFile = new File(path);
        if (ringtoneFile == null) {
            LogUtils.e("file is null");
            return;
        }
        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
        content.put(MediaStore.MediaColumns.TITLE, ringtoneFile.getName());
        content.put(MediaStore.MediaColumns.SIZE, ringtoneFile.length());
        content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        //  content.put(MediaStore.Audio.Media.ARTIST, "Madonna");
        //content.put(MediaStore.Audio.Media.DURATION, 230);
        content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        content.put(MediaStore.Audio.Media.IS_ALARM, false);
        content.put(MediaStore.Audio.Media.IS_MUSIC, false);
        // 获取文件是external还是internal的uri路径
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
        // 铃声通过contentvaues插入到数据库
        final Uri newUri = mContext.getContentResolver().insert(uri, content);
        RingtoneManager.setActualDefaultRingtoneUri(AppUtils.getAppContext(), RingtoneManager.TYPE_RINGTONE, newUri);
    }

    public void setRingtoneImpl2(String path) {
        File chosenFile = new File(path);
        if (chosenFile == null) {
            LogUtils.e("file is null");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, chosenFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, chosenFile.getName());
        values.put(MediaStore.MediaColumns.SIZE, chosenFile.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, true);
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.AudioColumns.IS_ALARM, false);
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(chosenFile.getAbsolutePath());
        mContext.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + chosenFile.getAbsolutePath() + "\"", null);

        Uri newUri = mContext.getContentResolver().insert(uri, values);

        try {
            RingtoneManager.setActualDefaultRingtoneUri(
                    mContext,
                    RingtoneManager.TYPE_RINGTONE,
                    newUri
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Settings.System.putString(mContext.getContentResolver(), "ringtone2", newUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Settings.System.putString(mContext.getContentResolver(), "ringtone_sim2", newUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Settings.System.putString(mContext.getContentResolver(), "ringtone_2", newUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getSettinsProviderStrin() {
        String[] rings = new String[]{"ringtone2", "ringtone_sim2","ringtone_2","ringtone"};

        for (int i = 0; i < rings.length; i++) {
            Uri uri = getUri(rings[i]);
            log("uri = " + uri);
//            LogUtils.e(" RingToneManager uri = " + uri);
        }
    }

    private void log(String msg) {
        LogUtils.e("RingToneManager", msg);
    }
    private Uri getUri(String c) {
        if (TextUtils.isEmpty(c)) {
            return null;
        }
        String string = Settings.System.getString(mContext.getContentResolver(), c);
        log("RingToneManager c = " + c + "get Uri string = " + string);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return Uri.parse(string);
    }

    /**
     * 删除铃声
     *
     * @param context
     * @param deleteUri
     * @param deleteFile
     */
    public void deleteCustomRingtone(Context context, Uri deleteUri, File deleteFile) {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        cv.put(MediaStore.Audio.Media.IS_ALARM, false);
        cv.put(MediaStore.Audio.Media.IS_MUSIC, true);
        // 更新当前铃声的数据，放弃作为铃声的状态
        context.getContentResolver().delete(deleteUri, MediaStore.MediaColumns.DATA + "=?",
                new String[]{deleteFile.getAbsolutePath()});
    }

    public void stopRing() {
//        if (mRingtoneManager != null) {
//            mRingtoneManager.setStopPreviousRingtone(true);
//            mRingtoneManager.stopPreviousRingtone();
//        }
//        if (mRingtone != null) {
//            mRingtone.stop();
//        }
    }

    public void playRing() {
//        mRingtone = RingtoneManager.getRingtone(mContext, Settings.System.DEFAULT_RINGTONE_URI);
//
//        mRingtone.setLooping(true);
//        mRingtone.play();
    }



    public static int setPermission(BaseActivity mActivity) {
        int num = 0;
        try {
            StringBuilder des = new StringBuilder();
            if (!PermissionUtils.checkFloatWindowPermission(mActivity)) {
                des.append(num2string(num) + "设置来电视频悬浮框");
                des.append("\n");
                num++;
            }

            if (!PermissionUtils.hasSettingPermisson(mActivity)) {
                des.append(num2string(num) + "修改铃声设置权限");
                des.append("\n");
                num++;
            }

            if (!PermissionUtils.isNotificationListenersEnabled(mActivity)) {
                des.append(num2string(num) + "设置来电通知权限");
                des.append("\n");
                num++;
            }

            if (!PermissionUtils.checkStartForeground(mActivity)) {
                des.append(num2string(num) + "开启后台弹出页面");
                des.append("\n");
                num++;
            }

            if (!PermissionUtils.checkLockedScreen(mActivity)) {
                des.append(num2string(num) + "开启来电显示");
                des.append("\n");
                num++;
            }

            if (num > 0) {
                if (mActivity != null && !mActivity.isDestroyed() && !mActivity.isFinishing()) {
                    RingPermissionDialog ringPermissionDialog = RingPermissionDialog.newInstance(mActivity.getPageName(),des.toString());
                    ringPermissionDialog.show(mActivity.getSupportFragmentManager(), "ring_permission_dialog");
                }
            }
        } catch (Exception e) {
        }

        return num;
    }


    public static String num2string(int num){
        if (num == 0) {
            return "① ";
        } else if (num == 1) {
            return "② ";
        } else if (num == 2) {
            return "③ ";
        } else if (num == 3) {
            return "④ ";
        } else if (num == 4) {
            return "⑤ ";
        } else if (num == 5) {
            return "⑥ ";
        }
        return "";
    }

}
