package com.huige.huigercode.net;

/**
 * Author : huiGer
 * Time   : 2018/6/25 0025 下午 05:34.
 * Desc   : 数据返回实体
 */
public class BaseResponse<T> {

    private int status; //状态吗
    private String msg; //返回的提示消息
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
