package com.kelin.retrofit2rxjava2.activity;

import android.app.Application;
import android.content.Context;
import com.kelin.retrofit2rxjava2.net.HttpsUtils;
import com.kelin.retrofit2rxjava2.net.RequetInterceptor;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 作者：PengJunShan.
 *
 * 时间：On 2019-05-06.
 *
 * 描述：初始化OkHttpClient  当然也可以在RetrofitCLient类中初始化
 */
public class MyApplication extends Application {

  public static Context context;
  public static OkHttpClient mOkHttpClient;
  /**
   * 超时时间
   */
  private static final int TIME_OUT = 30;

  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    initOkHttp();
  }

  private void initOkHttp() {
    //获取缓存路径
    File cacheDir = MyApplication.context.getExternalCacheDir();

    //设置缓存的大小
    int cacheSize = 10 * 1024 * 1024;
    //创建我们Client对象的构建者
    OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
    okHttpBuilder
        //为构建者设置超时时间
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        ////websocket轮训间隔(单位:秒)
        .pingInterval(20, TimeUnit.SECONDS)
        //设置缓存
        .cache(new Cache(cacheDir.getAbsoluteFile(), cacheSize))
        //允许重定向
        .followRedirects(true)
        //设置拦截器
        .addInterceptor(new RequetInterceptor())
        //添加https支持
        .hostnameVerifier(new HostnameVerifier() {
          @Override
          public boolean verify(String s, SSLSession sslSession) {
            return true;
          }
        })
        .sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());

    mOkHttpClient = okHttpBuilder.build();

  }

}
