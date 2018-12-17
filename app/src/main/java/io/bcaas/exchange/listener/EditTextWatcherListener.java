package io.bcaas.exchange.listener;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/31
 * 回調監聽：用於監聽輸入框的輸入完成
 */
public interface EditTextWatcherListener {
    void onComplete(String content);
}
