package com.jifen.dandan.common.view.ptr;

import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhangqiang.pageloader.ptr.loadmore.LoadMoreWidget;

public class SmartLoadMoreWidget extends LoadMoreWidget {

    private SmartRefreshLayout smartRefreshLayout;

    public SmartLoadMoreWidget(SmartRefreshLayout smartRefreshLayout) {
        this.smartRefreshLayout = smartRefreshLayout;
        smartRefreshLayout.setOnLoadMoreListener(new com.scwang.smartrefresh.layout.listener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                notifyLoadMore();
            }
        });
    }

    @Override
    protected void onLoadMoreComplete() {
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    protected void onLoadMoreError(Throwable e) {

    }

    @Override
    protected void onEnableChanged(boolean enable) {
        smartRefreshLayout.setEnableLoadMore(enable);
    }

    @Override
    public boolean isEnable() {
        return smartRefreshLayout.isEnableLoadMore();
    }
}
