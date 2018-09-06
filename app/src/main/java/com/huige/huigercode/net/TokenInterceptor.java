package com.huige.huigercode.net;

import android.util.Log;

import com.fengdi.zhaobei.bean.UserBean;
import com.fengdi.zhaobei.config.Constants;
import com.fengdi.zhaobei.config.ConstantsUrl;
import com.fengdi.zhaobei.module.mine.ui.LoginActivity;
import com.fengdi.zhaobei.utils.ActivityUtils;
import com.huige.library.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Author : huiGer
 * Time   : 2018/9/6 0006 上午 09:51.
 * Desc   : token失效, 重新登录
 */
public class TokenInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();

        // 判断刷新token连接，直接返回，不走下面代码，避免死循环。
        if (request.url().toString().contains("刷新token接口")) {
            return chain.proceed(request);
        }


        //拦截了响应体
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                return response;
            }
        }
        if (!isPlaintext(buffer)) {
            return response;
        }

        if (contentLength != 0) {
            // 获取到response的body的string字符串
            String result = buffer.clone().readString(charset);

            // {"status":2,"msg":"与服务器连接超时,请重新登录","data":null}
//            Log.d("msg", "TokenInterceptor -> intercept: 返回值是--> " + result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("status") == 2) { // token失效

                    if (!path.contains("刷新token接口")) { //代表不是刷新token
                        //获取新的token
                        HashMap<String, String> params = new HashMap<>();
                        params.put("mobileNo", (String) SharedPreferencesUtils.get("", ""));
                        params.put("bindNo", (String) SharedPreferencesUtils.get("", ""));
                        // 此处注释部分为同步请求
//                        BaseResponse<UserBean> execute = getApi(true).mobileLogin(params).execute().body();
                        Retrofit.Builder execute = new Retrofit.Builder();

                        // 下面部分为Server部分
//                        /**
//                         * 快捷登录
//                         * @param params
//                         * @return
//                         */
//                        @POST(ConstantsUrl.APP_MOBLIE_LOGIN)
//                        Call<BaseResponse<UserBean>> mobileLogin(@Body HashMap<String, String> params);

                        if (execute != null && execute.getStatus() == 1) {

                            // 重新组装
                            String urlStr = request.url().url().toString();
                            String[] split = urlStr.split("\\?");
                            Request.Builder requestBuilder = request.newBuilder();

                            if (request.method().equals("GET")) { // get
                                //TreeMap里面的数据会按照key值自动升序排列
                                TreeMap<String, String> param_map = new TreeMap<String, String>();
                                //获取参数对
                                String[] param_pairs = split[1].split("&");
                                for (String pair : param_pairs) {
                                    String[] param = pair.split("=");
                                    if (param.length != 2) {
                                        //没有value的参数不进行处理
                                        continue;
                                    }
                                    // 替换token
                                    if (param[0].equals("token")) {
                                        String token = execute.getData().getToken();
                                        Log.d("msg", "TokenInterceptor -> intercept: " + "GET请求 - token失效, 正在自动登录...");
                                        Log.d("msg", "TokenInterceptor -> intercept: 旧token--> " + param[1]);
                                        Log.d("msg", "TokenInterceptor -> intercept: 新token--> " + token);

                                        param_map.put(param[0], token);
                                        SharedPreferencesUtils.put(Constants.USER_TOKEN, token);
                                    } else {
                                        param_map.put(param[0], param[1]);
                                    }
                                }

                                Request newRequest = new Request.Builder().url(split[0]).build();
                                HttpUrl.Builder httpBuilder = newRequest.url().newBuilder();
                                for (String key : param_map.keySet()) {
                                    httpBuilder.addQueryParameter(key, param_map.get(key));
                                }
                                requestBuilder.url(httpBuilder.build());
                            } else { // post
                                RequestBody body = request.body();
                                if (body instanceof FormBody) {
                                    FormBody formBody = (FormBody) body;
                                    Map<String, String> formMap = new HashMap<>();
                                    // 从 formBody 中拿到请求参数，放入 formMap 中
                                    for (int i = 0; i < formBody.size(); i++) {
                                        // 替换token
                                        if (formBody.name(i).equals("token")) {
                                            String token = execute.getData().getToken();
                                            Log.d("msg", "TokenInterceptor -> intercept: " + "POST请求 - token失效, 正在自动登录...");
                                            Log.d("msg", "TokenInterceptor -> intercept: 旧token--> " + formBody.name(i));
                                            Log.d("msg", "TokenInterceptor -> intercept: 新token--> " + token);
                                            formMap.put(formBody.name(i), token);
                                        }else {
                                            formMap.put(formBody.name(i), formBody.value(i));
                                        }
                                    }

                                    // 重新修改 body 的内容
                                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                                    for (String key : formMap.keySet()) {
                                        bodyBuilder.add(key, formMap.get(key));
                                    }
                                    body = bodyBuilder.build();
                                }
                                if (body != null) {
                                    requestBuilder = request.newBuilder()
                                            .post(body);
                                }
                            }
                            return chain.proceed(requestBuilder.build()); //重新发起请求，此时是新的token
                        } else {
                            //要求用户直接登录
                            GotoLoginActivity();
                        }
                    } else {
                        return response;
                    }
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    //要求用户直接登录
    private void GotoLoginActivity() {
        ActivityUtils.getInstance().jumpActivity(LoginActivity.class);
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }


}
