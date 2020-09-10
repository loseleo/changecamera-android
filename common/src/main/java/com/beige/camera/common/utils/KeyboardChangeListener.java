package com.beige.camera.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by wangpeng on 2019/7/9.
 * 键盘弹出/隐藏监听
 */
public class KeyboardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private View mContentView;   // 当前界面的根视图
    private int mOriginHeight;   // 此时根视图的高度
    private int mPreHeight;   // 改变之前根视图的高度
    private KeyBoardListener mKeyBoardListen;

    public KeyboardChangeListener(Activity activity) {
        if (activity == null) {
            return;
        }
        mContentView = activity.getWindow().getDecorView();
        if (mContentView != null) {
            addContentTreeObserver();
        }
    }

    private void addContentTreeObserver() {
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        // 先获取到当前根视图的高度
        Rect r = new Rect();
        mContentView.getWindowVisibleDisplayFrame(r);
        int currHeight = r.height();
        if (currHeight == 0) {
            return;
        }

        boolean hasChange = false;
        if (mPreHeight == 0) {
            mPreHeight = currHeight;
            mOriginHeight = currHeight;
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true;
                mPreHeight = currHeight;
            } else {
                hasChange = false;
            }
        }
        if (hasChange) {
            boolean isShow;
            int keyboardHeight = 0;
            if (currHeight < mOriginHeight) {
                //隐藏
                keyboardHeight = mOriginHeight - currHeight;
                isShow = true;
            } else {
                //显示
                isShow = false;
            }
            if (mKeyBoardListen != null) {
                mKeyBoardListen.onKeyboardChange(isShow, keyboardHeight, r);
            }
        }
    }

    public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
        this.mKeyBoardListen = keyBoardListen;
    }

    // 资源释放
    public void destroy() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    public interface KeyBoardListener {

        void onKeyboardChange(boolean isShow, int keyboardHeight, Rect visibleArea);
    }
}
