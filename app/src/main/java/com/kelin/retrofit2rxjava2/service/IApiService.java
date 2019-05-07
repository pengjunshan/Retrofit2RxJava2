package com.kelin.retrofit2rxjava2.service;

import com.kelin.retrofit2rxjava2.bean.BannerBean;
import com.kelin.retrofit2rxjava2.bean.BaseBean;
import com.kelin.retrofit2rxjava2.bean.LoginBean;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 创建： PengJunShan on 2019-02-15 11:20.
 * 描述：
 */

public interface IApiService {

  /**
   * 获取Banner数据
   * @return
   */
  @GET("banner/json")
  Observable<BaseBean<List<BannerBean>>> getBanner();

  /**
   * 登录
   */
  @POST("user/login")
  @FormUrlEncoded
  Observable<BaseBean<LoginBean>> postLogin(@FieldMap Map<String, String> map);

  /**
   * 下载图片 无参
   */
  @GET("http://p0.meituan.net/165.220/movie/7f32684e28253f39fe2002868a1f3c95373851.jpg")
  Observable<ResponseBody> downLoadImg();

  /**
   * 下载图片有参
   */
  @POST("http://p0.meituan.net/165.220/movie/7f32684e28253f39fe2002868a1f3c95373851.jpg")
  @FormUrlEncoded
  Observable<ResponseBody> downLoadImg(@FieldMap Map<String, String> map);

}
