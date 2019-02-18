package io.bcaas.exchange.listener;

/**
 * @author catherine.brainwilliam
 * @since 2019/2/18
 * 回调监听：加载数据的回调
 */
public interface LoadingDataListener {
    //提供此方法触发加载数据的动作
    void onLoadingData();
}
