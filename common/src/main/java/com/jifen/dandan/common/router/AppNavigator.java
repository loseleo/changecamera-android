package com.jifen.dandan.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jifen.dandan.common.utils.LogUtils;


/**
 * app启动页面类
 * 构造Postcard之后需要加fixTaskFlag
 * Created by zhangqiang
 */
public class AppNavigator {


    public static void goMainActivity(Context context, String uriString, String tabName) {
        Postcard postcard = ARouter.getInstance().build(PageIdentity.APP_HOME);
//        postcard.setGroup(PageIdentity.GROUP_APP);
        postcard.withString("schemaUri", uriString)
                .withString("tabName", tabName)
                .navigation(context);
    }

    public static void goCameraActivity(Context context) {
        ARouter.getInstance().build(PageIdentity.APP_CAMERA).navigation(context);
    }

    public static void goImgPreviewActivity(Context context,String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_IMGPREVIEW)
                 .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goOldfffectActivity(Context context,String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_OLDFFFECT)
                 .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goWebViewActivity(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
//            ApiRequest.WebViewOptions options = new ApiRequest.WebViewOptions();
//            options.url = url;
//            AppUtils.openActivity(context, options);
        }
    }


    public static void goActivityByUri(Activity context, String uriString) {

        if (TextUtils.isEmpty(uriString) || !uriString.startsWith(PageIdentity.APP_SCHEME_HOST)) {
            return;
        }
        // 普通http uri，对应一个网页链接
        if (uriString.startsWith("http") && !uriString.contains(PageIdentity.APP_HOST)) {
            goWebViewActivity(context, uriString);
            return;
        }

        String path = uriString;
        String ddAppSchemeHost = PageIdentity.APP_SCHEME_HOST;
        // 需要中转的蛋蛋视频uri
        path = uriString.substring(ddAppSchemeHost.length(), uriString.contains("?") ? uriString.indexOf("?") : uriString.length());
        Postcard build = ARouter.getInstance().build(path);
        if (uriString.contains("?")) {
            String params = uriString.substring(uriString.indexOf("?") + 1);
            String[] keyValues = params.split("&");
            for (String str : keyValues) {
                String[] keyValue = str.split("=");
                build.withString(keyValue[0], keyValue[1]);
            }
        }
        build.navigation(context);
    }

}
