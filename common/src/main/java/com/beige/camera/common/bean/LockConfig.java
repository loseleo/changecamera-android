package com.beige.camera.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LockConfig implements Serializable {

    /**
     * lock_screen_switch_user_default : 1
     * lock_screen_switch_server : 1
     * lock_screen_jump : 1
     * title : title
     * desc : desc
     */
    @SerializedName("lock_screen_switch_user_default")
    private String lockScreenSwitchUserDefault;
    @SerializedName("lock_screen_switch_server")
    private String lockScreenSwitchServer;
    @SerializedName("lock_screen_jump")
    private String lockScreenJump;
    private String title;
    private String desc;

    public LockConfig(){
    }

    public LockConfig(String lockScreenSwitchUserDefault,String lockScreenSwitchServer,String lockScreenJump,String title,String desc){
        this.lockScreenSwitchUserDefault = lockScreenSwitchUserDefault ;
        this.lockScreenSwitchServer = lockScreenSwitchServer;
        this.lockScreenJump = lockScreenJump;
        this.title = title;
        this.desc = desc;
    }

    public String getLockScreenSwitchUserDefault() {
        return lockScreenSwitchUserDefault;
    }

    public void setLockScreenSwitchUserDefault(String lockScreenSwitchUserDefault) {
        this.lockScreenSwitchUserDefault = lockScreenSwitchUserDefault;
    }

    public String getLockScreenSwitchServer() {
        return lockScreenSwitchServer;
    }

    public void setLockScreenSwitchServer(String lockScreenSwitchServer) {
        this.lockScreenSwitchServer = lockScreenSwitchServer;
    }

    public String getLockScreenJump() {
        return lockScreenJump;
    }

    public void setLockScreenJump(String lockScreenJump) {
        this.lockScreenJump = lockScreenJump;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
