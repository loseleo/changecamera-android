package com.beige.camera.ringtone.core.loader;

public interface Callback<T> {

    void onSuccess(T t);

    void onFail(Throwable e);
}
