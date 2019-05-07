package com.kelin.retrofit2rxjava2.net;

import java.io.File;

/**
 * @author：PengJunShan.

 * 时间：On 2019-05-06.

 * 描述：流的回调接口
 */

public interface IResponseByteCallBack {

  //请求成功回调事件处理
  void onSuccess(File file);

  //请求失败回调事件处理
  void onFailure(String failureMsg);

}
