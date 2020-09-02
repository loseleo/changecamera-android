package com.jifen.dandan.ringtone.core.splash;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.ui.banner.ADBanner;
import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ringtone.core.infoflow.loader.CPCInfoFlowResourceLoader;
import com.jifen.dandan.ringtone.core.infoflow.loader.res.CPCResource;
import com.jifen.dandan.ringtone.core.loader.ResourceLoader;

public class CPCSplashAd extends CountDownSplashAd<CPCResource> {


    public CPCSplashAd(AdModel adModel, ViewGroup adContainer) {
        super(adModel, adContainer);
    }

    @Override
    protected void onSetupContentView(FrameLayout flAdContainer, CPCResource resource) {
        ADBanner mADBanner = new ADBanner(flAdContainer.getContext()) {

            float lastX, lastY;
            int touchSlop = -1;
            boolean dragged;

            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                if (touchSlop == -1) {
                    touchSlop = ViewConfiguration.get(flAdContainer.getContext()).getScaledTouchSlop();
                }
                int action = event.getAction();
                float currX = event.getX();
                float currY = event.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = currX;
                        lastY = currY;
                        dragged = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = currX - lastX;
                        float dy = currY - lastY;
                        float xDiff = Math.abs(dx);
                        float yDiff = Math.abs(dy);
                        if (!dragged) {
                            if (Math.abs(xDiff) > touchSlop || yDiff > touchSlop) {
                                dragged = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!dragged) {
                            notifyAdClick();
                        }
                        break;
                }
                return super.dispatchTouchEvent(event);
            }
        };
        mADBanner.UpdateView(resource.getiCliBundle());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mADBanner.setLayoutParams(layoutParams);
        flAdContainer.removeAllViews();
        flAdContainer.addView(mADBanner);
        notifyAdDisplay();
    }

    @Override
    protected int getCountDown() {
        return 3;
    }

    @NonNull
    @Override
    protected ResourceLoader<CPCResource> onCreateResourceLoader(AdModel adModel) {
        return new CPCInfoFlowResourceLoader<CPCResource>(adModel, getAdContainer().getContext()) {
            @NonNull
            @Override
            protected CPCResource convert(ICliBundle iCliBundle) {
                return new CPCResource(iCliBundle);
            }
        };
    }


}
