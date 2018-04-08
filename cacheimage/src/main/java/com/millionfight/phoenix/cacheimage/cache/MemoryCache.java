package com.millionfight.phoenix.cacheimage.cache;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public class MemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mLruCache;
    private static final int MAX_LRU_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);

    public MemoryCache() {
        //初始化LruCache
        initLruCache();
    }

    private void initLruCache() {
        mLruCache = new LruCache<String, Bitmap>(MAX_LRU_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mLruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if (TextUtils.isEmpty(url) || bitmap == null) {
            return;
        }
        mLruCache.put(url, bitmap);
    }

}
