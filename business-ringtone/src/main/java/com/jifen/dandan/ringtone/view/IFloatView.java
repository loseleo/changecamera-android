package com.beige.camera.advertisement.view;

import android.view.WindowManager;

/**
 * Description:悬浮窗抽象方法
 *
 * @author 杜乾-Dusan,Created on 2018/1/24 - 10:24.
 *         E-mail:duqian2010@gmail.com
 */
public interface IFloatView {
    FloatViewParams getParams();
    void setFloatViewListener(FloatViewListener listener);
    void setParams(FloatViewParams params);
    void setWindowParams(WindowManager.LayoutParams wmParams);
}
