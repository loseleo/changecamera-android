package com.beige.camera.bean;

import java.io.Serializable;

public class FunctionBean implements Serializable {

    public static String ID_CHANGE_OLD = "id_change_old";//变老
    public static String ID_CHANGE_GENDER_BOY = "id_change_gender_boy";//性别转换
    public static String ID_CHANGE_GENDER_GIRL = "id_change_gender_girl";//性别转换
    public static String ID_CHANGE_CHILD = "id_change_child";//变小孩
    public static String ID_CHANGE_CARTOON = "id_change_cartoon";//漫画脸
    public static String ID_CHANGE_ANIMAL = "id_change_animal";//动物检测
    public static String ID_DETECTION_AGE = "id_detection_age";//年龄检测
    public static String ID_DETECTION_BABY = "id_detection_baby";//宝宝检测
    public static String ID_DETECTION_VS = "id_detection_vs";//宝宝检测
    public static String ID_CHANGE_BACKGROUND = "id_change_background";//换背景
    public static String ID_DETECTION_PAST = "id_detection_past";//前世今生
    public static String ID_CHANGE_CLOTHES = "id_change_clothes";//一键换装
    public static String ID_CHANGE_HAIR = "id_change_hair";//换发型
    public static String ID_CHANGE_CUSTOMS = "id_change_customs";//异国风情
    public static String ID_CHANGE_ANIMALFACE = "id_change_animalface";//动物脸

    private String id;
    private String title;
    private String cover;
    private int drawable;
    private String imageUrl;
    private boolean isShowAD = false;

    public FunctionBean(String id, String title, int drawable) {
        this.id = id;
        this.title = title;
        this.drawable = drawable;
    }

    public FunctionBean(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isShowAD() {
        return isShowAD;
    }

    public void setShowAD(boolean showAD) {
        isShowAD = showAD;
    }

}
