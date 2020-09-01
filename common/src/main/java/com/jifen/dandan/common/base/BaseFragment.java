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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.zhangqiang.visiblehelper.FragmentVisibleHelper;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;
import com.zhangqiang.visiblehelper.VisibleHelperOwner;


public abstract class BaseFragment extends Fragment implements VisibleHelperOwner {

    private FragmentVisibleHelper visibleHelper = new FragmentVisibleHelper(this);
    private View mRootView;

    @LayoutRes
    public abstract int getLayoutResId();

    protected abstract void setupActivityComponent();

    public abstract void initViews(View contentView);

    public abstract void attachView();

    public abstract void initData();

    @NonNull
    public abstract String getPageName();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();

    public Context getApplicationContext() {
        Context context = getContext();
        return context == null ? null : context.getApplicationContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle state) {
        mRootView = inflater.inflate(getLayoutResId(), container, false);
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setupActivityComponent();
        handleArguments();
        super.onCreate(savedInstanceState);
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {

            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (isVisible) {
                } else {
                }
            }
        });
    }

    protected void handleArguments() {
        if (getArguments() != null) {
            onParseArguments(getArguments());
        }
    }

    public void onParseArguments(@NonNull Bundle args) {
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachView();
        initViews(view);
        initData();
        configViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        visibleHelper.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getContext());
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getContext());
        MobclickAgent.onPageEnd(getPageName());
    }

    @Override
    public void onStop() {
        super.onStop();
        visibleHelper.onStop();
    }

    @CallSuper
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visibleHelper.setUserVisibleHint();
    }

    @CallSuper
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        visibleHelper.onHiddenChanged();
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @NonNull
    @Override
    public FragmentVisibleHelper getVisibleHelper() {
        return visibleHelper;
    }


    @SuppressWarnings("TypeParameterUnusedInFormals")
    public <T extends View> T findViewById(@IdRes int id) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }

}