package com.huige.huigercode.net;

/**
 * Author : huiGer
 * Time   : 2018/6/25 0025 下午 06:05.
 * Desc   :
 */
public class ApiException extends Exception {
    private int code;
    private String message;
    private BaseResponse mBaseResponse;

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public ApiException(BaseResponse baseResponse) {
        this.mBaseResponse = baseResponse;
        this.code = baseResponse.getStatus();
        this.message = baseResponse.getMsg();
    }

    public int getCode() {
        return code;
    }

    public BaseResponse getBaseResponse() {
        return mBaseResponse;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
