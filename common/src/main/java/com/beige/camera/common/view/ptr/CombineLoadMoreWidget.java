package com.beige.camera.common.view.ptr;

import com.zhangqiang.pageloader.ptr.loadmore.LoadMoreWidget;

public class CombineLoadMoreWidget extends LoadMoreWidget {


    private LoadMoreWidget loadMoreWidget;
    private LoadMoreWidget loadMoreWidget2;
    private boolean loading;

    public CombineLoadMoreWidget(LoadMoreWidget loadMoreWidget, LoadMoreWidget loadMoreWidget2) {
        this.loadMoreWidget = loadMoreWidget;
        this.loadMoreWidget2 = loadMoreWidget2;
        loadMoreWidget.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                distinctLoadMore();
            }
        });
        loadMoreWidget2.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                distinctLoadMore();
            }
        });
    }

    private void distinctLoadMore() {
        if (loading) {
            return;
        }
        loading = true;
        notifyLoadMore();
    }

    @Override
    protected void onLoadMoreComplete() {
        loading = false;
        loadMoreWidget.setLoadMoreComplete();
        loadMoreWidget2.setLoadMoreComplete();
    }

    @Override
    protected void onLoadMoreError(Throwable e) {
        loadMoreWidget.setLoadMoreError(e);
        loadMoreWidget2.setLoadMoreError(e);
    }

    @Override
    protected void onEnableChanged(boolean enable) {
        loadMoreWidget.setEnable(enable);
        loadMoreWidget2.setEnable(enable);
    }

    @Override
    public boolean isEnable() {
        return loadMoreWidget.isEnable() || loadMoreWidget2.isEnable();
    }
}
