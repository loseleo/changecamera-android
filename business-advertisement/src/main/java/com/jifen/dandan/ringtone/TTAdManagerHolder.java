package com.jifen.dandan.ringtone;

import android.content.Context;

import com.bykv.vk.openvk.TTVfConfig;
import com.bykv.vk.openvk.TTVfConstant;
import com.bykv.vk.openvk.TTVfManager;
import com.bykv.vk.openvk.TTVfSdk;
import com.jifen.dandan.common.BuildConfig;
import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.common.utils.PackageUtils;


/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

    private static boolean sInit;

    public static TTVfManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTVfSdk.getVfManager();
    }

    public static void init(Context context) {
        doInit(context);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            TTVfSdk.init(context, buildConfig(context));
            sInit = true;
        }
    }

    private static TTVfConfig buildConfig(Context context) {
        String appId = BuildConfig.AD_TT_APP_ID;
        LogUtils.e("TTAdManagerHolder  appId =" + appId );
        return new TTVfConfig.Builder()
                .appId(appId)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(PackageUtils.getAppName(context))
                .titleBarTheme(TTVfConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTVfConstant.NETWORK_STATE_WIFI, TTVfConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(true)//是否支持多进程
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();
    }
}
