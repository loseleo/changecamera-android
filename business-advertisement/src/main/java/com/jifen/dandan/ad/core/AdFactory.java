package com.jifen.dandan.ad.core;

import com.jifen.dandan.common.feed.bean.AdModel;

public interface AdFactory<T extends Ad> {

    T create(AdModel adModel);
}
