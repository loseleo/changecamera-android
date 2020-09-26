package com.beige.camera.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class TemplatesConfigBean implements Serializable {


    private ArrayList<Template> templates;

    public ArrayList<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        this.templates = templates;
    }

    public static class Template implements Serializable {

        private String name;
        private String image;
        private String description;
        private String imageEffect;
        private boolean isShowAD = false;

        public Template(String name, String image) {
            this.name = name;
            this.image = image;
        }

        public String getName() {
            if (TextUtils.isEmpty(name)) {
                return image;
            }
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageEffect() {
            return imageEffect;
        }

        public void setImageEffect(String imageEffect) {
            this.imageEffect = imageEffect;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isShowAD() {
            return isShowAD;
        }

        public void setShowAD(boolean showAD) {
            isShowAD = showAD;
        }
    }
}
