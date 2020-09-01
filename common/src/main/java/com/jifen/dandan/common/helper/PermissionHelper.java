package com.jifen.dandan.common.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;

import com.jifen.dandan.common.utils.MmkvUtil;
import com.jifen.dandan.common.utils.ThreadUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mmkv.MMKV;

import io.reactivex.functions.Consumer;


/**
 * @author : suijin
 * e-mail : yusuijin@qutoutiao.net
 * time   : 2019/01/15
 * desc   :
 */
public class PermissionHelper {
    /**
     * 是否第一次 请求广告视频权限
     */
    private static final String IS_FIRST_EXECUTE_VIDEO_TASK = "isFirstExecuteVideoTask";

    private FragmentActivity activity;

    /**
     * 构造函数
     *
     * @param activity 所在 的 mContext
     */
    public PermissionHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    /**
     * 请求广告视频权限
     */
    public void requestVideoAdvPermission(PermissionCallback permissionCallback) {

        if (MmkvUtil.getInstance().getBoolean(IS_FIRST_EXECUTE_VIDEO_TASK, true)) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED) {
                grantPermissions(permissionCallback);
            } else {
                showDialog(permissionCallback);
            }
            MmkvUtil.getInstance().putBoolean(IS_FIRST_EXECUTE_VIDEO_TASK, false);
        } else {
            grantPermissions(permissionCallback);
        }
    }

    private void showDialog(final PermissionCallback permissionCallback) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示").setMessage("必须获取手机信息、定位权限，才能进行视频任务");
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    grantPermissions(permissionCallback);

                }
            });

            builder.create();
            builder.setCancelable(false);
            builder.show();

        } catch (Throwable e) {
            //
        }

    }

    private void showFailDialog() {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示").setMessage("必须获取手机信息、定位权限，才能进行视频任务");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivity(intent);
                    activity.finish();

                }
            });

            builder.create();
            builder.setCancelable(false);
            builder.show();

        } catch (Throwable e) {
            //
        }

    }


    @SuppressLint("CheckResult")
    private void grantPermissions(final PermissionCallback permissionCallback) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {

                    RxPermissions rxPermission = new RxPermissions(activity);
                    rxPermission.requestEachCombined(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {
                                // 用户已经同意该权限
                                permissionCallback.granted();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                showFailDialog();
                            } else {
                                // 用户拒绝了该权限，并且选中『不再询问』
                                showFailDialog();
                            }
                        }
                    });

                } catch (Throwable e) {
                    //
                }
            }
        });


    }


    /**
     * 视频广告权限授权
     */
    public interface PermissionCallback {
        /**
         * 授权成功回调
         */
        void granted();
    }


}
