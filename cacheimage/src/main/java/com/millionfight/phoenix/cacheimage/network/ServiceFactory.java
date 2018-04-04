package com.millionfight.phoenix.cacheimage.network;

import android.annotation.SuppressLint;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/4/4 0004.
 * Copyright (C) 2016 MillionFight
 * <p/>
 * Copy or sale of this class is forbidden.
 */

public class ServiceFactory {

    protected static final int HTTP_TIMEOUT_MS = 60 * 1000;
    private static final String SSL = "SSL";

    /**
     * 设置OkHttp信任所有证书
     */
    private static X509TrustManager xtm = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
        }
    };

    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance(SSL);
            sc.init(null, new TrustManager[]{xtm},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    /**
     * 设置网络客户端信息
     */
    public static OkHttpClient sOkHttpClient = new OkHttpClient.Builder()
            .sslSocketFactory(createSSLSocketFactory(), xtm)
            .hostnameVerifier(
                    new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }
            )
            .connectTimeout(HTTP_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .build();

    public static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl("https://www.millionfight.com/")
            .client(sOkHttpClient)
            .build();

    public static Services getServices() {
        return sRetrofit.create(Services.class);
    }

}
