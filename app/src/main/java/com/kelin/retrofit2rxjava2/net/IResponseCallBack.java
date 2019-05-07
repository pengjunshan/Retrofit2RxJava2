package com.kelin.retrofit2rxjava2.net;

/**
 * @author：PengJunShan.

 * 时间：On 2019-05-06.

 * 描述：自定义回调接口
 */

public interface IResponseCallBack<T> {

  void onSuccess(T data);

  void onFail(OkHttpException failuer);

}
