package com.millionfight.phoenix.cacheimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.millionfight.phoenix.cacheimage.cache.ImageCache;
import com.millionfight.phoenix.cacheimage.network.ServiceFactory;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public class ImageLoader {

    private ImageCache mImageCache;

    public ImageLoader(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    public void displayImage(String url, ImageView imageView, int defaultImageRes) {
        imageView.setImageResource(defaultImageRes);
        imageView.setTag(url);

        Bitmap bitmap = mImageCache.getBitmap(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            downloadImage(imageView, url);
        }
    }

    private void downloadImage(final ImageView imageView, final String url) {
        Call<ResponseBody> resultCall = ServiceFactory.getServices().downloadImage(url);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (TextUtils.equals((String) imageView.getTag(), url)) {
                        imageView.setImageBitmap(bitmap);
                    }
                    mImageCache.putBitmap(url, bitmap);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

}
