package com.jifen.dandan.screenlock.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.utils.MmkvUtil;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.utils.TimeUtils;
import com.jifen.dandan.screenlock.config.LockConfig;
import com.jifen.dandan.screenlock.widget.dialog.LockPermissionNavigateDialog;

/**
 * @author : suijin
 * e-mail : yusuijin@qutoutiao.net
 * time   : 2019/02/11
 * desc   : 管理锁屏 触发条件 与缓存
 */
public class LockManager {

    //是否有跳转
    public boolean isJump = false;


    public static synchronized LockManager getInstance() {
        return LockManagerHolder.mInstance;
    }


    public static final class LockManagerHolder {
        public static LockManager mInstance = new LockManager();
    }


    /**
     * 是否真的展示过锁屏页面
     *
     * @param bool
     */
    public void setIsRealShowLockScreen(boolean bool) {
        MmkvUtil.getInstance().putBoolean(LockConfig.LockSpKey.LOCK_SCREEN_IS_REAL_SHOW, bool);
    }

    /**
     * 是否展示开启锁屏权限UI
     */
    public boolean isShowOpenLockPermission() {

        boolean permission = MmkvUtil.getInstance().getBoolean(LockConfig.LockSpKey.LOCK_SCREEN_IS_REAL_SHOW, false);
        if (permission) {//已经开启权限的情况下不显示提示
            return false;
        }

        String lastDate = MmkvUtil.getInstance().getString(LockConfig.LockSpKey.LAST_SHOW_LOCK_SCREEN_TIPS_DATE);
        String currDate = TimeUtils.getCurrentDate();
        //如果上次展示的时间和当前时间是同一天，则返回false
        if (TextUtils.equals(currDate, lastDate)) {
            return false;
        }
        int showTimes = MmkvUtil.getInstance().getInt(LockConfig.LockSpKey.SHOW_LOCK_SCREEN_TIPS_TIMES, 0);
        //如果展示次数超过三次，则不展示
        if (showTimes >= 3) {
            return false;
        }

        //不符合上面所有条件，则展示，把当前展示时间、次数+1保存
        MmkvUtil.getInstance().putString(LockConfig.LockSpKey.LAST_SHOW_LOCK_SCREEN_TIPS_DATE, currDate);
        ++showTimes;
        MmkvUtil.getInstance().putInt(LockConfig.LockSpKey.SHOW_LOCK_SCREEN_TIPS_TIMES, showTimes);

        return true;
    }



    public static void showPermissionNavigateDialog(FragmentActivity activity, LockPermissionNavigateDialog.ClickCallback mClickCallback){
        boolean aBoolean = MmkvUtil.getInstance().getBoolean(LockConfig.LockSpKey.HAS_SHOW_LOCKPERMIASSION_GUID);
        if(!aBoolean){
            LockPermissionNavigateDialog dialog = new LockPermissionNavigateDialog();
            dialog.setClickCallback(mClickCallback);
            dialog.show(activity.getSupportFragmentManager(),"lock");
            MmkvUtil.getInstance().putBoolean(LockConfig.LockSpKey.HAS_SHOW_LOCKPERMIASSION_GUID, true);
        }



    }


    /**
     * 是否打开app
     *
     * @return
     */
    public boolean getJumpTo() {
        return MmkvUtil.getInstance().getBoolean(LockConfig.LockSpKey.LOCK_SCREEN_JUMP_TO, false);
    }


    /**
     * 判断当前手机是否锁屏
     *
     * @return
     */
    public boolean screenIsLock() {
        try {
            KeyguardManager mKeyguardManager = (KeyguardManager) BaseApplication.getsInstance().getBaseContext().getSystemService(Context.KEYGUARD_SERVICE);
            boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
            return flag;
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
    }


    /**
     * 中间home键跳转到app
     */
    public void jumpToApp() {

        if (isJump) {
            return;
        }
        if (!getJumpTo()) {
            return;
        }
        Intent appIntent = new Intent(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        Intent launchIntentForPackage = BaseApplication.getsInstance().getBaseContext().getPackageManager().getLaunchIntentForPackage(PackageUtils.getPackageName(BaseApplication.getsInstance().getBaseContext()));
        if (launchIntentForPackage != null) {
            appIntent.setComponent(launchIntentForPackage.getComponent());
        }
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        BaseApplication.getsInstance().getBaseContext().startActivity(appIntent);
    }



}