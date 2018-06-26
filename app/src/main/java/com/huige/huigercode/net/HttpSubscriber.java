package com.huige.huigercode.net;

import com.huige.huigercode.utils.CommonUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.DefaultSubscriber;
import retrofit2.HttpException;

/**
 * Author : huiGer
 * Time   : 2018/6/26 0026 上午 11:30.
 * Desc   :
 */
public abstract class HttpSubscriber<T> extends DefaultSubscriber<T>{

    public abstract void _onNext(T t);
    public abstract void _onError(String message);

    @Override
    public void onComplete() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {
        if(t != null) {
            _onNext(t);
        }else {
            CommonUtils.showToast("连接失败");
        }

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            CommonUtils.showToast("请打开网络");
        } else if (e instanceof SocketTimeoutException) {
            CommonUtils.showToast("请求超时");
        } else if (e instanceof ConnectException) {
            CommonUtils.showToast("连接失败");
        } else if (e instanceof IOException) {
            CommonUtils.showToast("连接失败");
        } else if (e instanceof HttpException) {
            CommonUtils.showToast("服务暂不可用");
        } else if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            CommonUtils.showToast(exception.getMessage());
        } else {
            CommonUtils.showToast("请求失败");
        }
        e.printStackTrace();

        _onError(e.getMessage());
    }

}

