package com.jifen.dandan.ad.core.splash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ad.R;
import com.jifen.dandan.ad.core.SimpleAdListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public abstract class CountDownSplashAd<T> extends SplashAd<T> {

    public CountDownSplashAd(AdModel adModel, ViewGroup adContainer) {
        super(adModel, adContainer);
    }

    private BehaviorSubject<Boolean> stopSubject = BehaviorSubject.createDefault(false);

    @Override
    protected void onDestroy() {
        stopCountDown();
    }

    @Override
    protected void onSetupAdResource(T t) {

        ViewGroup adContainer = getAdContainer();
        stopSubject.onNext(false);
        Context context = adContainer.getContext();
        View adView = LayoutInflater.from(context).inflate(R.layout.ad_splash, adContainer, false);

        View btSkip = adView.findViewById(R.id.bt_skip);
        TextView tvSkip = adView.findViewById(R.id.tv_skip);
        btSkip.setEnabled(false);
        btSkip.setVisibility(View.INVISIBLE);
        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdSkip();
                stopCountDown();
            }
        });
        adView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                stopCountDown();
            }
        });
        adContainer.removeAllViews();
        adContainer.addView(adView);

        addAdListener(new SimpleAdListener() {
            @Override
            public void onAdDisplay() {
                super.onAdDisplay();
                btSkip.setVisibility(View.VISIBLE);
                final int skipDuration = 2;
                final int skipCount = getCountDown();
                final int totalCount = skipCount + skipDuration;
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .take(totalCount + 1)
                        .map(new Function<Long, Long>() {
                            @Override
                            public Long apply(Long aLong) throws Exception {
                                return totalCount - aLong;
                            }
                        })
                        .takeUntil(stopSubject.filter(new Predicate<Boolean>() {
                            @Override
                            public boolean test(Boolean aBoolean) throws Exception {
                                return aBoolean;
                            }
                        }))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Long aLong) {
                                if (aLong <= skipDuration) {
                                    btSkip.setEnabled(true);
                                    tvSkip.setText(context.getResources().getString(R.string.ad_splash_skip));
                                } else {
                                    tvSkip.setText(String.valueOf(aLong - skipDuration));
                                }
                                if (aLong <= 0) {
                                    notifyAdTimeOver();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                notifyAdFail(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void onAdClick() {
                super.onAdClick();
                stopCountDown();
            }
        });
        FrameLayout flAdContainer = adView.findViewById(R.id.fl_ad_container);
        onSetupContentView(flAdContainer,t);
    }

    protected abstract void onSetupContentView(FrameLayout flAdContainer, T t);

    protected abstract int getCountDown();

    private void stopCountDown() {
        stopSubject.onNext(true);
    }

}
