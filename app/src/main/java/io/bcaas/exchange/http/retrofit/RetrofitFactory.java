package io.bcaas.exchange.http.retrofit;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.bcaas.exchange.bean.ServerBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.ServerTool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/20
 * <p>
 * Http：Retrofit封裝网络请求
 */
public class RetrofitFactory {

    private static Retrofit APIInstance;//访问正常訪問的网络
    private static Retrofit UpdateInstance;//检查更新
    private static OkHttpClient client;

    private static void initClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(Constants.ValueMaps.TIME_OUT_TIME_LONG, TimeUnit.SECONDS)
                    .readTimeout(Constants.ValueMaps.TIME_OUT_TIME_LONG, TimeUnit.SECONDS)
                    .writeTimeout(Constants.ValueMaps.TIME_OUT_TIME_LONG, TimeUnit.SECONDS)
                    .addInterceptor(new OkHttpInterceptor())
                    .build();
        }
    }

//    public static Retrofit getInstance() {
//        ServerBean serverBean = ServerTool.getDefaultServerBean();
//        if (serverBean == null) {
//            serverBean = ServerTool.getDefaultServerBean();
//            if (serverBean == null) {
//                return null;
//            }
//        }
//        return getSFNInstance(serverBean.getSfnServer());
//    }


    /**
     * Application api
     *
     * @return
     */
    public static Retrofit getAPIInstance() {
        initClient();
//        ServerBean serverBean = ServerTool.getDefaultServerBean();
        String apiServer = "http://192.168.0.163:8080/ExchangeApi/";
//        if (serverBean != null) {
//            apiServer = serverBean.getApiServer();
//        }
        APIInstance = new Retrofit.Builder()
                .baseUrl(apiServer)
                .client(client)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//Observable，暂时没用
                .build();
        return APIInstance;
    }

    /**
     * update server api
     *
     * @return
     */
    public static Retrofit getUpdateInstance() {
        initClient();
        ServerBean serverBean = ServerTool.getDefaultServerBean();
        String updateServer = null;
        if (serverBean != null) {
            updateServer = serverBean.getUpdateServer();
        }
        UpdateInstance = new Retrofit.Builder()
                .baseUrl(updateServer)
                .client(client)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//Observable，暂时没用
                .build();
        return UpdateInstance;
    }

    //清空当前所有请求的缓存数据信息
    public static void clean() {
        cleanAPI();
        UpdateInstance = null;
    }

    //清空API请求
    public static void cleanAPI() {
        APIInstance = null;
    }

}
