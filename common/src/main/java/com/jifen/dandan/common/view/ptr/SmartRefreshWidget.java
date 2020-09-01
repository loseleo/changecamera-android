package com.jifen.dandan.common.view.ptr;

import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhangqiang.pageloader.ptr.refresh.RefreshWidget;

public class SmartRefreshWidget extends RefreshWidget {

    private SmartRefreshLayout smartRefreshLayout;

    public SmartRefreshWidget(SmartRefreshLayout smartRefreshLayout) {
        this.smartRefreshLayout = smartRefreshLayout;
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                notifyRefresh();
            }
        });
    }

    @Override
    protected void onRefreshError(Throwable e) {

    }

    @Override
    protected void onRefreshComplete() {
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public boolean isEnable() {
        return smartRefreshLayout.isEnableRefresh();
    }

    @Override
    protected void onEnableChanged(boolean enable) {
        smartRefreshLayout.setEnableRefresh(enable);
    }
}
