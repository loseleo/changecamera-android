package com.beige.camera.bean;

import java.io.Serializable;

public class FunctionBean implements Serializable {

    public static String ID_CHANGE_OLD = "id_change_old";
    public static String ID_CHANGE_GENDER = "id_change_gender";
    public static String ID_CHANGE_CHILD = "id_change_child";
    public static String ID_CHANGE_CARTOON = "id_change_cartoon";
    public static String ID_CHANGE_ANIMAL = "id_changec_animal";
    public static String ID_DETECTION_AGE = "id_detection_age";

    private String id;
    private String title;
    private String cover;
    private int drawable;
    private String link;

    public FunctionBean(String id, String title, int drawable) {
        this.id = id;
        this.title = title;
        this.drawable = drawable;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
