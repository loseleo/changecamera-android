package com.jifen.dandan.common.base.bean;

public interface IApiResult<T> {

    int getCode();

    String getMessage();

    T getData();

    boolean isSuccess();

    long getServerTime();
}
