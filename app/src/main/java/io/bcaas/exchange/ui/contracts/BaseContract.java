package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.ResponseJson;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 */
public interface BaseContract {
    interface View {
        //显示加载框
        void showLoading();

        //隐藏加载框
        void hideLoading();

        //没有网路
        void noNetWork();
        //http 异常信息处理
        boolean httpExceptionDisposed(ResponseJson responseJson);
    }

    interface Presenter {
    }
}
