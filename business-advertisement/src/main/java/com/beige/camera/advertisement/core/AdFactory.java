package com.beige.camera.advertisement.core;

import com.beige.camera.common.feed.bean.AdModel;

public interface AdFactory<T extends Ad> {

    T create(AdModel adModel);
}
