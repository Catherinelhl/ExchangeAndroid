package io.bcaas.exchange.listener;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/28
 * <p>
 * 回调监听：「Chart图立回调监听」设置一个监听用于监听点击当前chart显示触摸点的信息
 */
public interface ChartMarkerViewListener {
    void getIndex(int index);
}
