package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.ui.contracts.FundPasswordContract;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/2
 * 「设置资金密码」
 */
public class FundPasswordPresenterImp implements FundPasswordContract.Presenter {
    private FundPasswordContract.View view;

    public FundPasswordPresenterImp(FundPasswordContract.View view) {
        super();
        this.view = view;
    }

    @Override
    public void setFundPassword(String password) {
        //如果当前资金密码只是本地记录，那么将当前密码存储在数据库，然后更换设备需要重新设置；也或者当前密码传给服务器。
        //当前资金密码设置成功之后，需要将当前程序标示flag设为true；
        BaseApplication.setFundPassword(true);

        view.setFundPasswordSuccess();
    }
}
