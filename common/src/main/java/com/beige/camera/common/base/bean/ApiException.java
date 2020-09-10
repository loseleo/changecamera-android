package com.beige.camera.common.base.bean;

public class ApiException extends RuntimeException {

    private int code;
    private long serverTime;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(int code, String message,long serverTime) {
        super(message);
        this.code = code;
        this.serverTime = serverTime;
    }

    public int getCode() {
        return code;
    }

    public long getServerTime() {
        return serverTime;
    }
}
