package com.jifen.dandan.screenlock.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.core.AdManager;
import com.beige.camera.advertisement.core.infoflow.InfoFlowAd;
import com.beige.camera.advertisement.core.strategy.Callback;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.jifen.dandan.screenlock.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author : suijin
 * e-mail : yusuijin@qutoutiao.net
 * time   : 2019/02/11
 * desc   :
 */
@Route(path = PageIdentity.APP_LOCK_LOCKACTIVITY)
public class LockReaderActivity extends BaseActivity {

    public static WeakReference<LockReaderActivity> instanceRef;

    public static LockReaderActivity getInstance() {
        return instanceRef != null ? instanceRef.get() : null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceRef = new WeakReference<>(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        closeSystemLock(LockReaderActivity.this);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.lock_main_activity;
    }

    @Override
    public void initViews() {
        TextView tvTime = findViewById(R.id.tv_time);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvWeek = findViewById(R.id.tv_week);
        FrameLayout adContainer = findViewById(R.id.ad_container);


        Date date = new Date(System.currentTimeMillis());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
        tvTime.setText(time);
        String dateStr = new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(date);
        tvDate.setText(dateStr);
        tvWeek.setText(getWeekStr());

        AdConfigBean adModel = (AdConfigBean) getIntent().getSerializableExtra("dataList");
        if (adContainer != null) {
            adContainer.post(new Runnable() {
                @Override
                public void run() {
                    AdManager.loadInfoFlowAd(adContainer, adModel.getCandidates(), new Callback<InfoFlowAd>() {
                        @Override
                        public void onAdLoadStart(InfoFlowAd ad) {
                        }

                        @Override
                        public void onFail(Throwable e) {
                        }
                    });
                }
            });
        }
    }


    @Override
    public void initData() {

    }

    @Override
    public void configViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_LOCK_LOCKACTIVITY;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * 关闭activity
     */
    public void closeActivity() {
        if (!isFinishing()) {
            finish();
        }
    }


    public void jumpToPersonalHomePage() {
//        UserBaseData userData = new UserBaseData();
//        userData.setAvatar(model.getMember().getAvatar());
//        userData.setMemberId(model.getMember().getMember_id());
//        userData.setNickname(model.getMember().getNickname());
//        userData.setSex(model.getMember().getSex());
//        userData.setFollowStatus(model.getFollow_status());
//        String uri = PageIdentity.DD_DDOPEN_SCHEME_HOST + PageIdentity.DD_PERSONAL_HOME_PAGE + "?" + DdConstants.PARAM_PERSON_HOME_USER_DATA + "=" + JSONUtils.toJSON(userData);
//        AppNavigator.goActivityByUri(getContext(), uri);
//        LockManager.getInstance().isJump = true;

        closeActivity();
    }

    public void openApp() {
//        String uri = PageIdentity.DD_DDOPEN_SCHEME_HOST + PageIdentity.DD_HOME;
//        AppNavigator.goActivityByUri(getContext(), uri);
//        LockManager.getInstance().isJump = true;
        closeActivity();
    }


    /**
     * 关闭系统锁屏
     */
    private static void closeSystemLock(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                activity.getClass().getMethod("setShowWhenLocked", boolean.class).invoke(activity, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null && !keyguardManager.isKeyguardSecure()) {
                keyguardManager.requestDismissKeyguard(activity, null);
            }
        } else {

            Window window = activity.getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        }

    }


    private String getWeekStr() {

        int week = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
        String weekString;
        switch (week) {
            case 1:
                weekString = "星期日";
                break;
            case 2:
                weekString = "星期一";
                break;
            case 3:
                weekString = "星期二";
                break;
            case 4:
                weekString = "星期三";
                break;
            case 5:
                weekString = "星期四";
                break;
            case 6:
                weekString = "星期五";
                break;
            case 7:
                weekString = "星期六";
                break;
            default:
                weekString = "星期日";
                break;
        }
        return weekString;
    }

}
