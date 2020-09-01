package com.jifen.dandan.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhangqiang.visiblehelper.FragmentVisibleHelper;
import com.zhangqiang.visiblehelper.VisibleHelperOwner;

import java.util.HashMap;
import java.util.Map;


/**
 * DialogFragment 弹窗基础类
 *
 * @author liyanxi
 * @date 3/29/19
 */
public abstract class BaseDialogFragment extends DialogFragment implements VisibleHelperOwner {

    private FragmentVisibleHelper visibleHelper = new FragmentVisibleHelper(this);
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnCancelListener onCancelListener;
    private static final Map<FragmentManager,FragmentRecord> mDialogPool = new HashMap<>();
    private boolean showWithQueue;

    protected boolean useFeatureBottomSheet() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWithQueue = showWithQueue();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        if (context == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return useFeatureBottomSheet() ? new BottomSheetDialog(context, getTheme()) : super.onCreateDialog(savedInstanceState);
    }

    /**
     * 复写父类dismiss方法，允许状态丢失
     */
    @Override
    public void dismiss() {
        try {
            dismissAllowingStateLoss();
        } catch (Throwable e) {
            //
        }
    }

    /**
     * 复写父类展示弹窗方法
     *
     * @param manager The FragmentManager this fragment will be added to.
     * @param tag     The tag for this fragment, as per
     */
    private void safeShow(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
            return;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {

            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        visibleHelper.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        visibleHelper.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visibleHelper.setUserVisibleHint();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        visibleHelper.onHiddenChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public BaseDialogFragment setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    @NonNull
    @Override
    public FragmentVisibleHelper getVisibleHelper() {
        return visibleHelper;
    }

    public void cancel() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!showWithQueue) {
            safeShow(manager, tag);
            return;
        }
        if (manager.isDestroyed()) {
            mDialogPool.remove(manager);
            return;
        }
        FragmentRecord mPool = mDialogPool.get(manager);
        if (mPool == null || mPool.fragment.getFragmentManager() == null) {
            //头结点为空或者头节点没有show，直接show
            safeShow(manager, tag);
            if (mPool == null) {
                mPool = new FragmentRecord(this,tag);
                mDialogPool.put(manager,mPool);
            }
        }else {
            //否则加入尾节点
            FragmentRecord tail = mPool;
            while (tail.next != null) {
                tail = tail.next;
            }
            tail.next = new FragmentRecord(this,tag);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
        if (showWithQueue) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentRecord mPool = mDialogPool.get(fragmentManager);
            if (mPool != null) {

                mPool = mPool.next;
                if (mPool != null) {
                    mDialogPool.put(fragmentManager,mPool);
                    mPool.fragment.show(fragmentManager, mPool.tag);
                }else {
                    mDialogPool.remove(fragmentManager);
                }
            }
        }
    }

    private static class FragmentRecord{

        private BaseDialogFragment fragment;
        private String tag;
        private FragmentRecord next;

        FragmentRecord(BaseDialogFragment fragment, String tag) {
            this.fragment = fragment;
            this.tag = tag;
        }
    }

    /**
     * 是否支持队列弹窗
     *
     * @return true表示排队显示，false表示不需要
     */
    protected boolean showWithQueue() {
        return false;
    }
}
