package com.beige.camera.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class BetterRecyclerView extends RecyclerView {

    private static final int INVALID_POINTER = -1;
    private int mScrollPointerId = INVALID_POINTER;
    private int mInitialTouchX, mInitialTouchY;
    private int mTouchSlop;

    public BetterRecyclerView(Context context) {
        this(context, null);
    }

    public BetterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BetterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public void setScrollingTouchSlop(int slopConstant) {
        super.setScrollingTouchSlop(slopConstant);
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        switch (slopConstant) {
            case TOUCH_SLOP_DEFAULT:
                mTouchSlop = vc.getScaledTouchSlop();
                break;
            case TOUCH_SLOP_PAGING:
                mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(vc);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        final int action = e.getActionMasked();
        final int actionIndex = e.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = (int) (e.getY() + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_POINTER_DOWN:
                mScrollPointerId = e.getPointerId(actionIndex);
                mInitialTouchX = (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY = (int) (e.getY(actionIndex) + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    return false;
                }

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                if (getScrollState() != SCROLL_STATE_DRAGGING) {
                    final int dx = x - mInitialTouchX;
                    final int dy = y - mInitialTouchY;
                    final boolean canScrollHorizontally = getLayoutManager().canScrollHorizontally();
                    final boolean canScrollVertically = getLayoutManager().canScrollVertically();
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > mTouchSlop && (Math.abs(dx) >= Math.abs(dy) || canScrollVertically)) {
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > mTouchSlop && (Math.abs(dy) >= Math.abs(dx) || canScrollHorizontally)) {
                        startScroll = true;
                    }
                    return startScroll && super.onInterceptTouchEvent(e);
                }
                return super.onInterceptTouchEvent(e);
            }

            default:
                return super.onInterceptTouchEvent(e);
        }
    }
}