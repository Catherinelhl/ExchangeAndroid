package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 */
public interface BaseContract {
    interface View {
        void showLoading();//显示加载框

        void hideLoading();//隐藏加载框

        void noNetWork();//没有网路
    }

    interface Presenter {

    }
}
