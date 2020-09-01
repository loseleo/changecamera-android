package com.jifen.dandan.ad.core.loader;

public interface Callback<T> {

    void onSuccess(T t);

    void onFail(Throwable e);
}
