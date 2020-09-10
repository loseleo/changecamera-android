package com.beige.camera.common.router;

import com.beige.camera.common.BuildConfig;

/**
 * Author: maxiaoxun@qutoutiao.net
 * Time: 2019/8/16 13:55
 */
public class PageIdentity {


    public static final String APP_HOST = BuildConfig.APP_HOST;
    public static final String APP_SCHEME_HOST = BuildConfig.APP_SCHEME + "://" + BuildConfig.APP_HOST;

    public static final String SERVICE_APP = "/app/service";
    public static final String GROUP_APP = "app";
    public static final String APP_WELCOME = "/app/welcome";
    public static final String APP_HOME = "/app/home";
    public static final String APP_CAMERA = "/app/camera";
    public static final String APP_IMGPREVIEW = "/app/imgpreview";
    public static final String APP_OLDEFFECT = "/app/oldeffect";
    public static final String APP_ANIMALEFFECT = "/app/animaleffect";
    public static final String APP_GENDEREFFECT = "/app/gendereffect";
    public static final String APP_AGEEFFECT = "/app/ageeffect";
    public static final String APP_CARTOONEFFECT = "/app/cartooneffect";
    public static final String APP_IMGUPLOAD = "/app/imgupload";




}