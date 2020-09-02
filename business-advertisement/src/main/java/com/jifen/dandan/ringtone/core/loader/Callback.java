package com.jifen.dandan.ringtone.core.loader;

public interface Callback<T> {

    void onSuccess(T t);

    void onFail(Throwable e);
}
