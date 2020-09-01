package com.jifen.dandan.ad.core.infoflow.loader.res;

import com.iclicash.advlib.core.ICliBundle;
import com.jifen.dandan.ad.core.goldcoin.GoldCoinOwner;
import com.jifen.dandan.ad.core.goldcoin.GoldCoinUtils;
import com.jifen.dandan.ad.core.infoflow.video.VideoInfoOwner;

public class CPCResource {

    private ICliBundle iCliBundle;

    public CPCResource(ICliBundle iCliBundle) {
        this.iCliBundle = iCliBundle;
    }

    public ICliBundle getiCliBundle() {
        return iCliBundle;
    }
}
