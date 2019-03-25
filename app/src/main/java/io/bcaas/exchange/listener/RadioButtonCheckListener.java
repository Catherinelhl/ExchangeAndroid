package io.bcaas.exchange.listener;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/25 18:00
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.listener
+--------------+---------------------------------
+ description  +   单选按钮状态变化监听
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public interface RadioButtonCheckListener {
    void onChange(boolean isCheck);
}
