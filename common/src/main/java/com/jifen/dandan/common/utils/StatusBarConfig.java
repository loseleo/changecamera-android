package com.jifen.dandan.common.utils;

import android.graphics.Color;

public class StatusBarConfig {


    private StatusBarConfig() {

    }

    /**
     * 是否采用沉浸式
     */
    public boolean translucent;
    /**
     * 顶部statusBarColor
     */
    public int statusBarColor;
    /**
     * 是否状态栏字体颜色为深色
     */
    public boolean darkTheme;
    /**
     * 是否采用fitsSystemWindows=true属性
     */
    public boolean fitsSystemWindows;
    /**
     * 对fitsSystemWindows=false 的是否对原有view添加一层和状态栏等高的view，主要为了避免修改原有布局
     */
    public boolean wrapStatusBar;

    public static class Builder {
        StatusBarConfig target = new StatusBarConfig();

        /**
         * init 所有默认值
         */
        public Builder() {
            setDarkTheme(true);
            setStatusBarColor(Color.WHITE);
            setWrapStatusBar(false);
            setFitsSystemWindows(true);
            setTranslucent(true);
        }

        public Builder setTranslucent(boolean translucent) {
            target.translucent = translucent;
            return this;
        }

        public Builder setStatusBarColor(int statusBarColor) {
            target.statusBarColor = statusBarColor;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            target.darkTheme = darkTheme;
            return this;
        }

        public Builder setWrapStatusBar(boolean wrapStatusBar) {
            target.wrapStatusBar = wrapStatusBar;
            return this;
        }

        public Builder setFitsSystemWindows(boolean fitsSystemWindows) {
            target.fitsSystemWindows = fitsSystemWindows;
            return this;
        }

        public StatusBarConfig build() {
            return target;
        }
    }


}
