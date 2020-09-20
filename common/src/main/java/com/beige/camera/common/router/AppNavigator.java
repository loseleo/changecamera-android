package com.beige.camera.common.router;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.beige.camera.common.BuildConfig;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.PermissionPageUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * app启动页面类
 * 构造Postcard之后需要加fixTaskFlag
 * Created by zhangqiang
 */
public class AppNavigator {


    public static void goMainActivity(Context context, String uriString) {
        Postcard postcard = ARouter.getInstance().build(PageIdentity.APP_HOME);
        postcard.withString("schemaUri", uriString)
                .navigation(context);
    }

    public static void goCameraActivity(BaseActivity context, String function) {

        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions.requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.e("getPermissions onSubscribe");
            }

            @Override
            public void onNext(Permission permission) {
                LogUtils.e("zhangning", "Permission = " + permission.toString());
                if (permission.granted) {
                    ARouter.getInstance().build(PageIdentity.APP_CAMERA)
                            .withString("function", function)
                            .navigation(context);

                } else if (!permission.shouldShowRequestPermissionRationale) {
                    new PermissionPageUtils(context).jumpPermissionPage();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public static void goCameraActivity(BaseActivity context, String function, int requestCode) {

        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions.requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.e("getPermissions onSubscribe");
            }

            @Override
            public void onNext(Permission permission) {
                LogUtils.e("zhangning", "Permission = " + permission.toString());
                if (permission.granted) {
                    ARouter.getInstance().build(PageIdentity.APP_CAMERA)
                            .withString("function", function)
                            .navigation(context, requestCode);

                } else if (!permission.shouldShowRequestPermissionRationale) {
                    new PermissionPageUtils(context).jumpPermissionPage();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public static void goImgPreviewActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_IMGPREVIEW)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goOldEfectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_OLDEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goBabyEfectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_BABYEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goBeautyVsEfectActivity(Context context, String imagePath,String imagePathVS) {
        ARouter.getInstance().build(PageIdentity.APP_BEAUTYVSEFFECT)
                .withString("image_path", imagePath)
                .withString("image_path_vs", imagePathVS)
                .navigation(context);
    }

    public static void goAnimalEffectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_ANIMALEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goAgeffectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_AGEEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goBackgroundffectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_BACKGROUNDEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goGenderEffectActivity(Context context, String imagePath, String function) {
        ARouter.getInstance().build(PageIdentity.APP_GENDEREFFECT)
                .withString("image_path", imagePath)
                .withString("function", function)
                .navigation(context);
    }

    public static void goCartoonEffectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_CARTOONEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }

    public static void goUserCenterActivity(Context context) {
        ARouter.getInstance().build(PageIdentity.APP_USERCENTER)
                .navigation(context);
    }

    public static void goAboutUsActivity(Context context) {
        ARouter.getInstance().build(PageIdentity.APP_ABOUTUS)
                .navigation(context);
    }

    public static void goImgUploadActivity(Context context, String imagePath, String function) {
        ARouter.getInstance().build(PageIdentity.APP_IMGUPLOAD)
                .withString("image_path", imagePath)
                .withString("function", function)
                .navigation(context);
    }

    public static void goTwoImgUploadActivity(Context context, String imagePath, String function) {
        ARouter.getInstance().build(PageIdentity.APP_TWOIMGUPLOAD)
                .withString("image_path", imagePath)
                .withString("function", function)
                .navigation(context);
    }

    public static void goPastEffectActivity(Context context, String imagePath) {
        ARouter.getInstance().build(PageIdentity.APP_PASTEFFECT)
                .withString("image_path", imagePath)
                .navigation(context);
    }


    public static void goWebViewActivity(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            ARouter.getInstance().build(PageIdentity.APP_WEBVIEW)
                    .withString("url", url)
                    .navigation(context);
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
        // 需要中转的uri
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
