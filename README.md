# CacheImage
一个基于LRU的图片三级缓存库  
基于LruCache与DiskLruCache封装的图片三级缓存库。  
可以快速实现有特殊需求的图片缓存功能。  
相关文章介绍：[Android经典面试问题：请你设计一套图片异步加载缓存方案——图片的三级缓存](https://blog.csdn.net/arimakisho/article/details/79808320)
### 1、如何使用
#### (1)初始化ImageLoader对象，并传入缓存策略
```java
//MemoryAndDiskCache：同时使用内存缓存和本地缓存策略
//MemoryCache：只使用内存缓存策略
//DiskCache：只使用本地缓存策略
ImageLoader imageLoader = new ImageLoader(
  new MemoryAndDiskCache(getApplicationContext())
);
```
#### (2)调用ImageLoader的display方法，传入图片的url地址，要显示到的ImageView，默认图片资源
```java
String url = "";
imageLoader.displayImage(url, iv, R.mipmap.ic_launcher);
```
