package com.kelin.retrofit2rxjava2.net;

import android.content.Context;
import android.os.Environment;
import com.kelin.retrofit2rxjava2.utitl.Constants;
import com.kelin.retrofit2rxjava2.activity.MyApplication;
import com.kelin.retrofit2rxjava2.service.IApiService;
import com.kelin.retrofit2rxjava2.utitl.Utils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author：PengJunShan.

 * 时间：On 2019-05-06.

 * 描述：Retrofit对象
 */

public class RetrofitClient {

  public static Retrofit mRetrofit;

  /**
   * 为我们的Client配置参数，使用静态语句块来配置
   * 只执行一次，运行一开始就开辟了内存，内存放在全局
   */
  static {
    mRetrofit = new Retrofit.Builder()
        .baseUrl(Constants.BASEURL)//添加BaseUrl
        .client(MyApplication.mOkHttpClient)//添加OkhttpClient
        .addConverterFactory(GsonConverterFactory.create())//添加Gson解析
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//  添加 rxjava 支持
        .build();
  }

  /**
   * 所有请求都放在一个接口中
   */
  public static IApiService createApi() {
    return mRetrofit.create(IApiService.class);
  }

  /**
   * 不同请求放不同接口中
   * @param service 指定接口
   */
  public static <T> T createApi(Class<T> service) {
    return mRetrofit.create(service);
  }


  /**
   * @param context 上下文
   * @param observable 被观察者
   * @param listener 回调接口
   * @param requstName 接口名称
   * @param <T> 实体类
   */
  public static <T> void request(Context context, Observable<T> observable,
      final IResponseCallBack<T> listener, String requstName) {
    Constants.requestName = requstName;
    if (!Utils.isConnected(MyApplication.context)) {
      if (listener != null) {
        listener.onFail(new OkHttpException(-1, "网络不可用,请检查网络"));
      }
      return;
    }
    observable.subscribeOn(Schedulers.io())//指定Observable自身在io线程中执行
        .observeOn(AndroidSchedulers.mainThread())//指定一个观察者在主线程中国观察这个Observable
        .subscribe(new CallBackObserver<T>(listener, context));//订阅

  }

  /**
   * 统一下载图片共用
   * @param observable 被观察者
   * @param callback 回调接口
   * @param imgPath 存储地址
   * @param requstName 功能名称
   */
  public static void downImg(Observable<ResponseBody> observable,
      final IResponseByteCallBack callback, final String imgPath, String requstName) {
    Constants.requestName = requstName;
    observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ResponseBody responseBody) {
            File file = null;
            try {
              InputStream is = responseBody.byteStream();
              int len = 0;
              // 文件夹路径
              String pathUrl =
                  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                      + "/PJS/";
              File filepath = new File(pathUrl);
              if (!filepath.exists()) {
                filepath.mkdirs();// 创建文件夹
              }
              file = new File(pathUrl, imgPath);

              FileOutputStream fos = new FileOutputStream(file);

              byte[] buf = new byte[2048];
              while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
              }
              fos.flush();
              fos.close();
              is.close();
              callback.onSuccess(file);
            } catch (final Exception e) {
              callback.onFailure(e.getMessage());
            }
          }

          @Override
          public void onError(Throwable e) {
            callback.onFailure(e.getMessage());
          }

          @Override
          public void onComplete() {
          }
        });

  }

}
