package com.jifen.dandan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.jifen.dandan.common.router.AppNavigator;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

/**
 * 外部调起应用入口
 *
 * @author liyanxi
 * @date 4/9/19
 */
public class DepthLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uriString = null;
        if (getIntent() != null && getIntent().getData() != null) {
            uriString = getIntent().getData().toString();
        }
        try {
            if (!isTaskRoot() || (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {//
                if (!TextUtils.isEmpty(uriString)) {
                    AppNavigator.goActivityByUri(this,uriString);
                }
            } else {
                AppNavigator.goMainActivity(this,uriString,"");
            }
        } catch (Throwable e) {
            CrashReport.postCatchedException(e);
            e.printStackTrace();
        } finally {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
//        MobclickAgent.onPause(this);
    }
}
