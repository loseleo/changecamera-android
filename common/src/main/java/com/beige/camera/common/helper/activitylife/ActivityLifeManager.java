package com.beige.camera.common.helper.activitylife;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.beige.camera.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * activity 活跃状态管理者
 */
public class ActivityLifeManager {

    private static final ActivityLifeManager appShowManager = new ActivityLifeManager();
    private boolean isRunInBackground;
    private int activeActivityCount;
    private final List<AppShowListener> appShowListeners = new ArrayList<>();
    private List<OnTopActivityChangeListener> onTopActivityChangeListeners;

    private List<Activity> activityList = new ArrayList<>();

    private ActivityLifeManager() {
    }

    public static ActivityLifeManager get() {
        return appShowManager;
    }

    private boolean isInit;

    public synchronized void init(Application application) {
        if (isInit) {
            return;
        }
        isInit = true;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                setTopActivity(activity);
                if (isRunInBackground) {
                    isRunInBackground = false;
                    onAppReturnForeground();
                    LogUtils.e(" ActivityLifeManager ===== 热启动");

                } else if (activeActivityCount == 0) {
                    onAppEnterForeground();
                    LogUtils.e(" ActivityLifeManager ===== 冷启动");
                }
                activeActivityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (topActivity == activity) {
                    setTopActivity(null);
                }
                activeActivityCount--;
                if (activeActivityCount == 0) {
                    isRunInBackground = true;
                    onAppEnterBackground();
                    LogUtils.e(" ActivityLifeManager ===== 后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }
        });
    }

    public boolean isRunInBackground() {
        return isRunInBackground;
    }

    public int getActiveActivityCount() {
        return activeActivityCount;
    }

    public void addAppShowListener(AppShowListener appShowListener) {
        appShowListeners.add(appShowListener);
    }

    public void removeAppShowListener(AppShowListener appShowListener) {
        appShowListeners.remove(appShowListener);
    }


    private void onAppEnterForeground() {

        for (int i = 0; i < appShowListeners.size(); i++) {
            appShowListeners.get(i).onAppEnterForeground();
        }
    }

    private void onAppReturnForeground() {

        for (int i = 0; i < appShowListeners.size(); i++) {
            appShowListeners.get(i).onAppReturnForeground();
        }
    }


    private void onAppEnterBackground() {
        for (int i = 0; i < appShowListeners.size(); i++) {
            appShowListeners.get(i).onAppEnterBackground();
        }
    }

    /**
     * 退出app，清除所有的activity
     */
    public void clearAllActivity() {
        for (Activity activity : activityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        if (activityList != null) {
            activityList.clear();
        }
    }

    Activity topActivity;

    public Activity getTopActivity() {
//        Activity activity = activityList.get(activityList.size() - 1);
        return topActivity;
    }

    public Activity getActivity(int position) {
        if (position >= activityList.size()) {
            position = activityList.size() -1;
        }
        if(position < 0){
            position = 0;
        }
        return activityList.get(position);
    }

    public interface OnTopActivityChangeListener{

        void onTopActivityChanged();
    }

    public void addOnTopActivityChangeListener(OnTopActivityChangeListener listener){
        if (onTopActivityChangeListeners == null) {
            onTopActivityChangeListeners = new ArrayList<>();
        }
        if (onTopActivityChangeListeners.contains(listener)) {
            return;
        }
        onTopActivityChangeListeners.add(listener);
    }

    public void removeOnTopActivityChangeListener(OnTopActivityChangeListener listener){
        if (onTopActivityChangeListeners == null) {
            return;
        }
        onTopActivityChangeListeners.remove(listener);
    }

    private void setTopActivity(Activity activity){
        this.topActivity = activity;
        if (onTopActivityChangeListeners != null) {
            for (int i = onTopActivityChangeListeners.size() - 1; i >= 0; i--) {
                onTopActivityChangeListeners.get(i).onTopActivityChanged();
            }
        }
    }
}
