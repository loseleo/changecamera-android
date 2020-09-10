/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beige.camera.common.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.base.bean.ApiException;
import com.beige.camera.common.base.bean.IApiResult;
import com.zhangqiang.mvp.Destroyable;
import com.zhangqiang.mvp.DestroyableOwner;
import com.zhangqiang.mvp.FullLifecycleObserver;
import com.zhangqiang.mvp.OnDestroyListener;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;


public class RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_main() {    //compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable rxCreateDiskObservable(final String key, final Type type) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                LogUtils.d("get data from disk: key==" + key);
                String json = ACache.get(BaseApplication.getsInstance()).getAsString(key);
                LogUtils.d("get data from disk finish , json==" + json);
                if (!TextUtils.isEmpty(json)) {
                    emitter.onNext(json);
                }
                emitter.onComplete();
            }
        })
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String s) throws Exception {
                        return new Gson().fromJson(s, type);
                    }
                })
                .onErrorResumeNext(Observable.empty())
                .subscribeOn(Schedulers.io());
    }


    //缓存list到本地
    public static <T> ObservableTransformer<T, T> rxCacheListHelper(final String key) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Consumer<T>() {
                            @Override
                            public void accept(T data) throws Exception {
                                Schedulers.io().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        //通过反射获取List,再判空决定是否缓存
                                        if (data == null)
                                            return;
                                        Class clazz = data.getClass();
                                        Field[] fields = clazz.getFields();
                                        for (Field field : fields) {
                                            String className = field.getType().getSimpleName();
                                            // 得到属性值
                                            if (className.equalsIgnoreCase("List")) {
                                                try {
                                                    List list = (List) field.get(data);
                                                    LogUtils.d("list==" + list);
                                                    if (list != null && !list.isEmpty()) {
                                                        ACache.get(BaseApplication.getsInstance())
                                                                .put(key, new Gson().toJson(data, clazz));
                                                        LogUtils.d("cache finish");
                                                    }
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    //缓存javabean到本地
    public static <T> ObservableTransformer<T, T> rxCacheBeanHelper(final String key) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                        .doOnNext(new Consumer<T>() {
                            @Override
                            public void accept(final T data) throws Exception {
                                Schedulers.io().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        ACache.get(BaseApplication.getsInstance())
                                                .put(key, new Gson().toJson(data, data.getClass()));
                                        LogUtils.d("cache finish");
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public static <T> Function<IApiResult<T>, T> applyApiResult() {
        return new Function<IApiResult<T>, T>() {
            @Override
            public T apply(IApiResult<T> apiResult) throws Exception {
                if (apiResult.isSuccess()) {
                    return apiResult.getData();
                }
                throw new ApiException(apiResult.getCode(), apiResult.getMessage(),apiResult.getServerTime());
            }
        };
    }

    public static <T> Function<IApiResult<List<T>>, List<T>> applyApiResultList() {
        return new Function<IApiResult<List<T>>, List<T>>() {
            @Override
            public List<T> apply(IApiResult<List<T>> apiResult) throws Exception {
                if (apiResult.isSuccess()) {
                    List<T> data = apiResult.getData();
                    if (data == null) {
                        data = new ArrayList<>();
                    }
                    return data;
                }
                throw new ApiException(apiResult.getCode(), apiResult.getMessage(),apiResult.getServerTime());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> bindLifecycle(LifecycleOwner lifecycleOwner) {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {

                Lifecycle lifecycle = lifecycleOwner.getLifecycle();
                BehaviorSubject<Boolean> abortSubject = BehaviorSubject.createDefault(lifecycle.getCurrentState() == Lifecycle.State.DESTROYED);
                FullLifecycleObserver observer = new FullLifecycleObserver() {
                    @Override
                    public void onDestroy(@NonNull LifecycleOwner owner) {
                        super.onDestroy(owner);
                        abortSubject.onNext(true);
                    }
                };
                return upstream
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                lifecycle.addObserver(observer);
                            }
                        })
                        .doOnDispose(new Action() {
                            @Override
                            public void run() throws Exception {
                                lifecycle.removeObserver(observer);
                            }
                        })
                        .takeUntil(abortSubject.filter(new Predicate<Boolean>() {
                            @Override
                            public boolean test(Boolean aBoolean) throws Exception {
                                return aBoolean;
                            }
                        }));
            }
        };
    }

    public static <T> ObservableTransformer<T, T> bindDuringViewAttach(View view) {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {

                BehaviorSubject<Boolean> abortSubject = BehaviorSubject.createDefault(false);
                View.OnAttachStateChangeListener attachStateChangeListener = new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        abortSubject.onNext(true);
                    }
                };
                return upstream
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                view.addOnAttachStateChangeListener(attachStateChangeListener);
                            }
                        })
                        .doOnDispose(new Action() {
                            @Override
                            public void run() throws Exception {
                                view.removeOnAttachStateChangeListener(attachStateChangeListener);
                            }
                        })
                        .takeUntil(abortSubject.filter(new Predicate<Boolean>() {
                            @Override
                            public boolean test(Boolean aBoolean) throws Exception {
                                return aBoolean;
                            }
                        }));
            }
        };
    }

    public static <T> ObservableTransformer<T, T> bindUntilDestroyed(DestroyableOwner destroyableOwner) {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {

                Destroyable destroyable = destroyableOwner.getDestroyable();
                BehaviorSubject<Boolean> abortSubject = BehaviorSubject.createDefault(destroyable.isDestroyed());
                OnDestroyListener onDestroyListener = new OnDestroyListener() {
                    @Override
                    public void onDestroy() {
                        abortSubject.onNext(true);
                    }
                };
                return upstream
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                                destroyable.addOnDestroyListener(onDestroyListener);
                            }
                        })
                        .doOnDispose(new Action() {
                            @Override
                            public void run() throws Exception {
                                destroyable.removeOnDestroyListener(onDestroyListener);
                            }
                        })
                        .takeUntil(abortSubject.filter(new Predicate<Boolean>() {
                            @Override
                            public boolean test(Boolean aBoolean) throws Exception {
                                return aBoolean;
                            }
                        }));
            }
        };
    }

    public static class SimpleObserver<T> implements Observer<T> {


        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    public static Observable<View> viewClick(View view) {

        return Observable.create(new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(ObservableEmitter<View> emitter) throws Exception {

                try {

                    view.setOnClickListener(new View.OnClickListener() {
                        boolean processing = false;

                        @Override
                        public void onClick(View v) {
                            if (processing) {
                                return;
                            }
                            processing = true;
                            emitter.onNext(v);
                            processing = false;
                        }
                    });
                } catch (Throwable e) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        view.setOnClickListener(null);
                    }
                });
            }
        });
    }

    public static <T> ObservableTransformer<T, T> applyTimeFilter(int filterTime) {

        return new ObservableTransformer<T, T>() {
            Predicate<T> predicate = new Predicate<T>() {
                long time;

                @Override
                public boolean test(T t) throws Exception {
                    long currentTimeMillis = System.currentTimeMillis();

                    if (currentTimeMillis - time > filterTime) {
                        this.time = currentTimeMillis;
                        return true;
                    }
                    return false;
                }
            };

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.filter(predicate);
            }
        };
    }
}
