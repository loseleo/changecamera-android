package com.beige.camera.advertisement.core.loader;

public interface Callback<T> {

    void onSuccess(T t);

    void onFail(Throwable e);
}
