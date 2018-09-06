package com.huige.huigercode.net;


import com.huige.huigercode.App;
import com.huige.huigercode.BuildConfig;
import com.huige.huigercode.utils.CommonUtils;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author : huiGer
 * Time   : 2018/6/25 0025 下午 06:20.
 * Desc   :
 */
public class RetrofitHelper {

    /**
     * host
     */
    public static final String HOST = "";

    /**
     * 缓存
     */
    public static final String PATH_CACHE = App.getContext().getCacheDir().getAbsolutePath() + File.separator + "data" + File.separator + "NetCache";

    private static Api api;
    private static OkHttpClient okHttpClient = null;
    private static Retrofit retrofit;

    public static Api getApi() {
        return getApi(HOST);
    }

    public static Api getApi(String host) {

        if (api == null) {
            synchronized (RetrofitHelper.class) {
                if (api == null) {
                    retrofit = new Retrofit.Builder()
                            .client(getOkHttpClientInstance())
                            .baseUrl(host)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    api = retrofit.create(Api.class);
                }
                return api;
            }
        }
        return api;
    }

    private static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                        builder.addInterceptor(loggingInterceptor);
                    }
                    File cacheFile = new File(PATH_CACHE);
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
                    Interceptor cacheInterceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (!CommonUtils.isNetworkConnected()) {
                                request = request.newBuilder()
                                        .cacheControl(CacheControl.FORCE_CACHE)
                                        .build();
                            }
                            int tryCount = 0;
                            Response response = chain.proceed(request);
                            while (!response.isSuccessful() && tryCount < 3) {
                                tryCount++;

                                // retry the request
                                response = chain.proceed(request);
                            }

                            if (CommonUtils.isNetworkConnected()) {
                                int maxAge = 0;
                                // 有网络时, 不缓存, 最大保存时长为0
                                response.newBuilder()
                                        .header("Cache-Control", "public, max-age=" + maxAge)
                                        .removeHeader("Pragma")
                                        .build();
                            } else {
                                // 无网络时，设置超时为4周
                                int maxStale = 60 * 60 * 24 * 28;
                                response.newBuilder()
                                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                        .removeHeader("Pragma")
                                        .build();
                            }
                            return response;
                        }
                    };
                    //设置缓存
                    builder.addNetworkInterceptor(cacheInterceptor);
                    builder.addInterceptor(cacheInterceptor);
                    builder.cache(cache);
                    //设置超时
                    builder.connectTimeout(10, TimeUnit.SECONDS);
                    builder.readTimeout(20, TimeUnit.SECONDS);
                    builder.writeTimeout(20, TimeUnit.SECONDS);
                    //错误重连
                    builder.retryOnConnectionFailure(true);
                    // 添加请求头, 需要取消注释就好
//                    builder.addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request request = chain.request();
//                            // 添加请求头
//                            Request.Builder requestBuilder = request.newBuilder()
//                                    .addHeader("", "");
//                            return chain.proceed(requestBuilder.build());
//                        }
//                    });
                    okHttpClient = builder.build();
                }
                return okHttpClient;
            }
        }
        return okHttpClient;
    }

    public static <T> FlowableTransformer<BaseResponse<T>, T> handleResult() {
        return new FlowableTransformer<BaseResponse<T>, T>() {
            @Override
            public Publisher<T> apply(@NonNull final Flowable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {
                        if (tBaseResponse.getStatus() == 1) {
                            return createData(tBaseResponse.getData());
                        }
                        return Flowable.error(new ApiException(tBaseResponse.getStatus(), tBaseResponse.getMsg()));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Flowable<T> createData(final T data) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> flowableEmitter) throws Exception {
                try {
                    flowableEmitter.onNext(data);
                    flowableEmitter.onComplete();
                } catch (Exception e) {
                    flowableEmitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 控制线程
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_main(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
