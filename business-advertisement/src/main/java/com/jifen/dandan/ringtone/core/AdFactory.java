package com.jifen.dandan.ringtone.core;

import com.jifen.dandan.common.feed.bean.AdModel;

public interface AdFactory<T extends Ad> {

    T create(AdModel adModel);
}
