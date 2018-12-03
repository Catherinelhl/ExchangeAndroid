package io.bcaas.exchange.http.callback;

import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.tools.LogTool;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 自定義Http請求結果回應過濾
 *
 * @param <T>
 */
public abstract class BcaasCallback<T extends Object> implements retrofit2.Callback<T> {
    private String TAG = BcaasCallback.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        int code = response.raw().code();
        if (code == MessageConstants.CODE_404
                || code == MessageConstants.CODE_400) {
            LogTool.d(TAG, "internet response: " + MessageConstants.CODE_404);
            onNotFound();
        } else {
            if (response.raw().isSuccessful()) {
                onSuccess(response);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }


    public abstract void onSuccess(Response<T> response);

    public void onNotFound() {
        return;
    }
}