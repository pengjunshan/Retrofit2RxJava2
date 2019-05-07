package com.kelin.retrofit2rxjava2.bean;

import java.io.Serializable;

/**
 * @author：PengJunShan.

 * 时间：On 2019-05-06.

 * 描述：根据实际接口文档创建一个BaseBean 用泛型接收不同的实体类
 */

public class BaseBean<T> implements Serializable{

  private static int SUCCESS_CODE = 0;//接口访问成功的errorCode
  private int errorCode;
  private String errorMsg;
  private T data;
  /**
   * 是否成功获取到数据
   */
  public boolean isSuccess() {
    return getErrorCode() == SUCCESS_CODE;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "BaseBean{" +
        "errorCode=" + errorCode +
        ", errorMsg='" + errorMsg + '\'' +
        ", data=" + data +
        '}';
  }
}
