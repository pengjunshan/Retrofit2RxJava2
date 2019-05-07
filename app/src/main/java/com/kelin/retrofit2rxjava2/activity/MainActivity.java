package com.kelin.retrofit2rxjava2.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.kelin.retrofit2rxjava2.R;
import com.kelin.retrofit2rxjava2.bean.BannerBean;
import com.kelin.retrofit2rxjava2.bean.BaseBean;
import com.kelin.retrofit2rxjava2.bean.LoginBean;
import com.kelin.retrofit2rxjava2.net.IResponseByteCallBack;
import com.kelin.retrofit2rxjava2.net.IResponseCallBack;
import com.kelin.retrofit2rxjava2.net.OkHttpException;
import com.kelin.retrofit2rxjava2.service.IApiService;
import com.kelin.retrofit2rxjava2.utitl.Constants;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.List;
import java.util.Map;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private Context context;
  // 权限
  private static final int REQUEST_PERMISSION = 1;
  private static String[] PERMISSIONS = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_SETTINGS,
      Manifest.permission.INTERNET,
      Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
  };
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    context = this;
    initPermission();
  }

  /**
   * 封装之前
   * 简单使用
   */
  public void RetrofitRxJavaRequet(View view) {
    //第一步：创建Retrofit
    Retrofit mRetrofit = new Builder()
        .baseUrl(Constants.BASEURL)//添加BaseUrl
        .client(MyApplication.mOkHttpClient)//添加OkhttpClient
        .addConverterFactory(GsonConverterFactory.create())//添加Gson解析
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//  添加 rxjava 支持
        .build();

    //第二步：构建接口
    IApiService iApiService = mRetrofit.create(IApiService.class);

    //第三步：构建被观察者对象
    Observable<BaseBean<List<BannerBean>>> observable = iApiService.getBanner();

    //第四步：订阅
    observable.subscribeOn(Schedulers.io())//指定Observable自身在io线程中执行
        .observeOn(AndroidSchedulers.mainThread())//指定一个观察者在主线程中国观察这个Observable
        .subscribe(new Observer<BaseBean<List<BannerBean>>>() {
          @Override
          public void onSubscribe(Disposable d) {
            Log.e("TAG", "开始之前");
          }

          @Override
          public void onNext(BaseBean<List<BannerBean>> listBaseBean) {
            Toast.makeText(MainActivity.this, listBaseBean.getData().toString(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "成功");
          }

          @Override
          public void onError(Throwable e) {
            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "失败");
          }

          @Override
          public void onComplete() {
            Log.e("TAG", "结束");
          }
        });

  }

  /**
   * 封装后使用
   * GET请求
   */
  public void GetRequet(View view) {

    RetrofitRequest.getBannerApi(context, new IResponseCallBack<BaseBean<List<BannerBean>>>() {
      @Override
      public void onSuccess(BaseBean<List<BannerBean>> data) {
        Toast.makeText(MainActivity.this, "banner成功="+data.getData().toString(), Toast.LENGTH_SHORT).show();
        Log.e("TAG", "banner内容="+data.getData().toString());
      }

      @Override
      public void onFail(OkHttpException failuer) {
        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "失败="+failuer.getEmsg());
      }
    });

  }

  /**
   * 封装后使用
   * POST请求
   */
  public void PostKeyValueRequet(View view) {

    Map<String,String> map  = new ArrayMap<>();
    map.put("username", "15294792877");
    map.put("password", "15294792877pp");
    RetrofitRequest.postLoginApi(context, map, new IResponseCallBack<BaseBean<LoginBean>>() {
      @Override
      public void onSuccess(BaseBean<LoginBean> data) {
        Toast.makeText(MainActivity.this, "登录成功="+data.getData().toString(), Toast.LENGTH_SHORT).show();
        Log.e("TAG", "登录成功="+data.getData().toString());
      }

      @Override
      public void onFail(OkHttpException failuer) {
        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "失败="+failuer.getEmsg());
      }
    });

  }

  /**
   * 封装后使用
   * 下载图片
   */
  public void GetImgRequet(View view) {

    RetrofitRequest.downImgApi(String.valueOf(System.currentTimeMillis()) + ".png",
        new IResponseByteCallBack() {
          @Override
          public void onSuccess(File file) {
            Toast.makeText(MainActivity.this, "图片下载成功="+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "图片下载成功="+file.getAbsolutePath());
          }

          @Override
          public void onFailure(String failureMsg) {
            Toast.makeText(MainActivity.this, "图片下载失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "图片下载失败="+failureMsg);
          }
        });
  }



  /**
   * 初始化权限
   */
  private void initPermission() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      boolean isGranted = true;
      for (String permission : PERMISSIONS) {
        int result = ActivityCompat.checkSelfPermission(this, permission);
        if (result != PackageManager.PERMISSION_GRANTED) {
          isGranted = false;
          break;
        }
      }
      if (!isGranted) {
        // 还没有的话，去申请权限
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_PERMISSION) {
      boolean granted = true;
      for (int result : grantResults) {
        granted = result == PackageManager.PERMISSION_GRANTED;
        if (!granted) {
          break;
        }
      }
      if (!granted) {
        // 没有赋予权限
      } else {
        // 已经赋予过权限了
      }
    }
  }
}
