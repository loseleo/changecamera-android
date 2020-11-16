package com.beige.camera.screenlock.config;

/**
 * @author: zhanglin
 * @date: 2019/5/1
 * Copyright (c) 2019 https://www.qutoutiao.net. All rights reserved.
 */
public class LockConfig {

    /**
     * 锁屏状态
     * 1：开启
     * 2：关闭
     */
    public interface LockStatus{
        String OPEN_LOCK_SCREEN = "1";//开启
        String CLOSE_LOCK_SCREEN = "0";//关闭
    }

    /**
     * 锁屏sp保存所有的key
     */
    public interface LockSpKey{

        /**
         * 锁屏阅读设置开关 服务器
         * boolean true:开启，false：关闭
         */
        String LOCK_SCREEN_SWITCH_FROM_SERVER = "lock_screen_switch_from_server";
        /**
         * 锁屏阅读设置开关 boolean true:开启，false：关闭
         */
        String LOCK_SCREEN_SWITCH_FROM_USER = "lock_screen_switch_from_user";

        /**
         * 用户默认是否开启锁屏
         */
        String LOCK_SCREEN_SWITCH_USER_DEFAULT = "lock_screen_switch_user_default";

        /**
         * 锁屏书籍点击跳转页面
         */
        String LOCK_SCREEN_JUMP_TO = "lock_screen_jump_to";

        String LOCK_SCREEN_CLOSE_TITLE = "lock_screen_close_title";

        String LOCK_SCREEN_CLOSE_DESC = "lock_screen_close_desc";

        /**
         * 是否真的展示了锁屏页面。
         * 通过用户点击判断，如果用户点击了。则设置为true
         */
        String LOCK_SCREEN_IS_REAL_SHOW = "lock_screen_is_real_show";

        /**
         * 上一次展示锁屏UI的日期
         */
        String LAST_SHOW_LOCK_SCREEN_TIPS_DATE = "last_show_lock_screen_tips_date";

        /**
         * 一共展示了多少次
         */
        String SHOW_LOCK_SCREEN_TIPS_TIMES = "show_lock_screen_tips_times";

        String HAS_SHOW_LOCKPERMIASSION_GUID = "has_show_lockpermiassion_guid";

    }

}
