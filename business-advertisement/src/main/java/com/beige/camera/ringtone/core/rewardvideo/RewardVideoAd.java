package com.beige.camera.ringtone.core.rewardvideo;

import android.app.Activity;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.core.Ad;
import com.beige.camera.ringtone.core.dismiss.DismissedHelper;
import com.beige.camera.ringtone.core.dismiss.DismissedHelperOwner;

import java.util.ArrayList;
import java.util.List;

public abstract class RewardVideoAd<T> extends Ad<T> implements DismissedHelperOwner {

    private final List<OnRewardVerifyListener> onRewardVerifyListeners = new ArrayList<>();
    private final Activity activity;
    private final DismissedHelper dismissedHelper = new DismissedHelper();
    private boolean rewardVerify;

    public RewardVideoAd(Activity activity, AdModel adModel) {
        super(adModel);
        this.activity = activity;
    }


    @Override
    protected final void onSetupAdResource(T t) {
        onSetupAdResource(activity,t);
    }

    protected abstract void onSetupAdResource(Activity activity, T t);

    @Override
    protected void onDestroy() {

    }


    public void addOnRewardVerifyListener(OnRewardVerifyListener onRewardVerifyListener) {
        if (onRewardVerifyListener == null) {
            return;
        }
        if (this.onRewardVerifyListeners.contains(onRewardVerifyListener)) {
            return;
        }
        this.onRewardVerifyListeners.add(onRewardVerifyListener);
    }

    public void removeOnRewardVerifyListener(OnRewardVerifyListener onRewardVerifyListener) {
        if (onRewardVerifyListener == null) {
            return;
        }
        this.onRewardVerifyListeners.remove(onRewardVerifyListener);
    }

    protected void notifyReward(boolean rewardVerify) {
        this.rewardVerify = rewardVerify;
        for (int i = onRewardVerifyListeners.size() - 1; i >= 0; i--) {
            onRewardVerifyListeners.get(i).onRewardVerify(rewardVerify);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public DismissedHelper getDismissedHelper() {
        return dismissedHelper;
    }

    @Override
    public T getAdResource() {
        return super.getAdResource();
    }

    public boolean isRewardVerify() {
        return rewardVerify;
    }
}
