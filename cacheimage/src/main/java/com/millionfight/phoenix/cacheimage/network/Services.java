package com.millionfight.phoenix.cacheimage.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public interface Services {

    @Streaming
    @GET
    Call<ResponseBody> downloadImage(@Url String url);

}
