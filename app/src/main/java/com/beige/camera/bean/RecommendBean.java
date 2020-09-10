package com.beige.camera.bean;

import java.io.Serializable;

public class RecommendBean implements Serializable {

    private String id;
    private String title;
    private String cover;
    private int drawable;
    private String link;

    public RecommendBean() { }


    public RecommendBean(String id, String title, String cover, int drawable, String link) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.drawable = drawable;
        this.link = link;
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

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
