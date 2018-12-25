package io.bcaas.exchange.http.retrofit;


import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.NetWorkTool;
import io.bcaas.exchange.tools.PreferenceTool;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author catherine.brainwilliam
 * @since 2018-08-20
 * <p>
 * Http： 设置网络请求拦截器，用於獲取查看發送之前以及獲取到Response的原始信息
 */
public class OkHttpInterceptor implements Interceptor {

    private String TAG = OkHttpInterceptor.class.getSimpleName();

    private static final Charset UTF8 = Charset.forName(MessageConstants.CHARSET_FORMAT);

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder()
                .addHeader("cookie", BaseApplication.getStringFromSP("cookie"))
                .build();
        RequestBody requestBody = request.body();

        String body = null;

        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }
        // 获得Connection，内部有route、socket、handshake、protocol方法
        Connection connection = chain.connection();
        // 如果Connection为null，返回HTTP_1_1，否则返回connection.protocol()
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        // 比如: --> POST http://121.40.227.8:8088/api http/1.1
        String requestStartMessage = request.method() + ' ' + request.url() + ' ' + protocol;

        //"\nheaders:" + request.headers() +
        LogTool.d(TAG, requestStartMessage + "\nhttp request:" + body);

        // 打印 Response
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            if (NetWorkTool.connectTimeOut(e)) {
                //切换服务器
                LogTool.d(TAG, request.url() + ":\n" + MessageConstants.CONNECT_EXCEPTION);
            } else {
                LogTool.d(TAG, request.url() + ":\n" + e.getMessage());
            }
            throw e;
        }
        //-------------获取Response响应的数据然后获取cookies-------------
        List<String> setCookie = response.headers("set-cookie");
        if (ListTool.noEmpty(setCookie)) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Disposable subscribe = Observable.fromArray(setCookie)
                    .subscribe(strings -> {
                        for (String cookie : strings) {
                            LogTool.d(TAG, cookie);
                            cookieBuffer.append(cookie).append(";");
                        }
                    });

            BaseApplication.setStringToSP("cookie", cookieBuffer.toString());
            LogTool.d(TAG, "获取到的cookies是：" + cookieBuffer.toString());
        }
        //-------------获取Response响应的数据然后获取cookies-------------
        ResponseBody responseBody = response.body();
        long contentLength = 0;
        if (responseBody != null) {
            contentLength = responseBody.contentLength();
        }
        if (bodyEncoded(response.headers())) {

        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            if (contentLength != 0) {
                // 获取Response的body的字符串 并打印
                LogTool.d(TAG, " http response " + request.url() + "\n" + buffer.clone().readString(charset));
            }
        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get(MessageConstants.HTTP_CONTENT_ENCODING);
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase(MessageConstants.ENCODE_INGORE_CASE);
    }
}
