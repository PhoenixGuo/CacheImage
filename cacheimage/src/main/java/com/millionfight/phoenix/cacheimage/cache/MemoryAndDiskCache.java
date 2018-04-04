package com.millionfight.phoenix.cacheimage.cache;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public class MemoryAndDiskCache implements ImageCache {

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public MemoryAndDiskCache(Context context) {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = mMemoryCache.getBitmap(url);
        if (bitmap != null) {
            return bitmap;
        } else {
            bitmap = mDiskCache.getBitmap(url);
            return bitmap;
        }
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.putBitmap(url, bitmap);
        mDiskCache.putBitmap(url, bitmap);
    }

}
