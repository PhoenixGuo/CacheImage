package com.millionfight.phoenix.cacheimage.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.millionfight.phoenix.cacheimage.util.Md5Util;

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
        String bitmapUrlMD5 = Md5Util.getMD5String(url);
        return mLruCache.get(bitmapUrlMD5);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String bitmapUrlMD5 = Md5Util.getMD5String(url);
        mLruCache.put(bitmapUrlMD5, bitmap);
    }

}
