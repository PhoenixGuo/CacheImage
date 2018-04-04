package com.millionfight.phoenix.cacheimage.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.millionfight.phoenix.cacheimage.util.DiskLruCache;
import com.millionfight.phoenix.cacheimage.util.Md5Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public class DiskCache implements ImageCache {

    private DiskLruCache mDiskLruCache;
    private static final String DISK_LRU_CACHE_UNIQUE = "Image";
    private static final int MAX_DISK_LRU_CACHE_SIZE = 10 * 1024 * 1024;

    ExecutorService mExecutorsService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public DiskCache(Context context) {
        //初始化DiskLruCache
        initDiskLruCache(context);
    }

    private void initDiskLruCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(
                    context,
                    DISK_LRU_CACHE_UNIQUE
            );
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(
                    cacheDir,
                    getAppVersion(context),
                    1,
                    MAX_DISK_LRU_CACHE_SIZE
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    0
            );
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public Bitmap getBitmap(String url) {
        String bitmapUrlMD5 = Md5Util.getMD5String(url);
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(bitmapUrlMD5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, final Bitmap bitmap) {
        final String bitmapUrlMD5 = Md5Util.getMD5String(url);
        mExecutorsService.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        writeFileToDisk(mDiskLruCache, bitmap, bitmapUrlMD5);
                    }
                }
        );
    }

    private static void writeFileToDisk(
            DiskLruCache diskLruCache,
            Bitmap bitmap,
            String bitmapUrlMD5
    ) {
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = diskLruCache.edit(bitmapUrlMD5);
            if (editor != null) {
                outputStream = editor.newOutputStream(0);
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    editor.commit();
                }
            }
        } catch (Exception e) {
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                diskLruCache.flush();
            } catch (Exception e) {

            }
        }
    }

}
