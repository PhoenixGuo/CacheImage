package com.millionfight.phoenix.cacheimage.cache;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public interface ImageCache {

    Bitmap getBitmap(String url);

    void putBitmap(String url, Bitmap bitmap);

}
