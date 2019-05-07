package com.kelin.retrofit2rxjava2.net;

import android.app.Activity;
import android.content.Context;
import com.kelin.retrofit2rxjava2.bean.BaseBean;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author：PengJunShan.
 *
 * 时间：On 2019-05-06.
 *
 * 描述：公共Observer 观察者
 */
public class CallBackObserver<T> implements Observer<T> {

  private IResponseCallBack<T> mListener;
  private Activity activity;

  public CallBackObserver(IResponseCallBack<T> listener, Context context) {
    this.mListener = listener;
    this.activity = (Activity) context;
  }

  @Override
  public void onSubscribe(Disposable d) {
    /**
     * 这里可以 显示加载圈等
     */
  }

  /**
   * 成功
   * @param data
   */
  @Override
  public void onNext(T data) {
    if (mListener != null && !activity.isFinishing()) {
      BaseBean baseBean = (BaseBean) data;
      /**
       * 是否成功
       */
      if (baseBean.isSuccess()) {
        mListener.onSuccess(data);
      }else {
        mListener.onFail(new OkHttpException(baseBean.getErrorCode(), baseBean.getErrorMsg()));
      }
    }

  }

  /**
   * 失败
   * @param e
   */
  @Override
  public void onError(Throwable e) {
    onComplete();
    if (mListener != null && !activity.isFinishing()) {
      /**
       * 处理失败原因
       */
      OkHttpException okHttpException = ExceptionHandle.handleException(e,activity);
      if(okHttpException !=null) {
        mListener.onFail(okHttpException);
      }
    }

  }

  @Override
  public void onComplete() {
    /**
     * 这里可以 关闭加载圈等
     */
  }

}
