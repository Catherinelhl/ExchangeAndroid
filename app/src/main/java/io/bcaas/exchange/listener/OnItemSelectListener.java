package io.bcaas.exchange.listener;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/16
 * 回調監聽：各種list彈框、多類別條目选择器監聽回調響應
 */
public interface OnItemSelectListener {
    /**
     * 返回當前選擇欄目的數據類
     */
    <T> void onItemSelect(T type, String from);

}
