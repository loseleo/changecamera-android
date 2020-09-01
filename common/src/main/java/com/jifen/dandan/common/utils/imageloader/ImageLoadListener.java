package com.jifen.dandan.common.utils.imageloader;

import android.support.annotation.Nullable;

public interface ImageLoadListener {

    void onDisplay(ImageInfo imageInfo);

    void onFail(@Nullable Throwable e);

}
