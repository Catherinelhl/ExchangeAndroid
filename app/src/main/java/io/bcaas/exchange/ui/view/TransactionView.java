package io.bcaas.exchange.ui.view;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/4/15 16:53
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.view
+--------------+---------------------------------
+ description  +   V1.2 「交易」界面
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import android.content.Context;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseLinearLayout;

public class TransactionView extends BaseLinearLayout {
    private String TAG = TransactionView.class.getSimpleName();

    public TransactionView(Context context) {
        super(context);
    }

    @Override
    protected int setContentView() {
        return R.layout.view_transaction;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }
}
