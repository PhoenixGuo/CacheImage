package com.millionfight.phoenix.cacheimagedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.millionfight.phoenix.cacheimage.ImageLoader;
import com.millionfight.phoenix.cacheimage.cache.MemoryAndDiskCache;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522862776296&di=8ebc4953ad6c8099048dea2f40d2a71c&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201507%2F14%2F20150714233157_ErSWZ.jpeg";
        ImageLoader imageLoader = new ImageLoader(
                new MemoryAndDiskCache(getApplicationContext())
        );
        imageLoader.displayImage(url, iv, R.mipmap.ic_launcher);
    }

}
