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
package com.jifen.dandan.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jifen.dandan.common.utils.StatusBarConfig;
import com.umeng.analytics.MobclickAgent;
import com.zhangqiang.visiblehelper.ActivityVisibleHelper;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;
import com.zhangqiang.visiblehelper.VisibleHelper;
import com.zhangqiang.visiblehelper.VisibleHelperOwner;

public abstract class BaseActivity extends AppCompatActivity implements VisibleHelperOwner {

    private final ActivityVisibleHelper visibleHelper = new ActivityVisibleHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreWhenCreate(savedInstanceState);
        } else {
            ARouter.getInstance().inject(this);
        }
        int layoutResId = getLayoutResId();
        if (layoutResId > 0) {
            setContentView(layoutResId);
        }
        setupActivityComponent();

        initViews();
        attachView();
        initData();
        configViews();
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (!isVisible) {
                }
            }
        });
    }

    protected void onRestoreWhenCreate(Bundle savedInstanceState) {

    }

    protected abstract void setupActivityComponent();

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract void initViews();

    public void attachView() {

    }

    public abstract void initData();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();

    public StatusBarConfig getStatusBarConfig() {
        return new StatusBarConfig.Builder().build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        visibleHelper.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        visibleHelper.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public VisibleHelper getVisibleHelper() {
        return visibleHelper;
    }


    @Nullable
    public abstract String getPageName();

}
