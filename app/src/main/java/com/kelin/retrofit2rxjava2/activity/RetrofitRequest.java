package com.kelin.retrofit2rxjava2.activity;

import android.content.Context;
import com.kelin.retrofit2rxjava2.bean.BannerBean;
import com.kelin.retrofit2rxjava2.bean.BaseBean;
import com.kelin.retrofit2rxjava2.bean.LoginBean;
import com.kelin.retrofit2rxjava2.net.IResponseByteCallBack;
import com.kelin.retrofit2rxjava2.net.IResponseCallBack;
import com.kelin.retrofit2rxjava2.net.RetrofitClient;
import com.kelin.retrofit2rxjava2.service.IApiService;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;

/**
 * 作者：PengJunShan.
 *
 * 时间：On 2019-05-06.
 *
 * 描述：
 */
public class RetrofitRequest {

  /**
   * 获取Banner数据
   */
  public static void getBannerApi(Context context, IResponseCallBack<BaseBean<List<BannerBean>>> callback) {
    Observable<BaseBean<List<BannerBean>>> observable = RetrofitClient.createApi().getBanner();
    RetrofitClient.request(context, observable, callback, "获取Banner");
  }

  /**
   * 登录
   */
  public static void postLoginApi(Context context,Map<String,String> map,IResponseCallBack<BaseBean<LoginBean>> callBack){
    Observable<BaseBean<LoginBean>> observable = RetrofitClient.createApi(IApiService.class)
        .postLogin(map);
    RetrofitClient.request(context,observable,callBack,"登录");
  }

  /**
   * 下载图片
   */
  public static void downImgApi(String imgPath,IResponseByteCallBack callBack){
    Observable<ResponseBody> observable = RetrofitClient.createApi().downLoadImg();
    RetrofitClient.downImg(observable,callBack,imgPath,"下载图片");
  }

}
