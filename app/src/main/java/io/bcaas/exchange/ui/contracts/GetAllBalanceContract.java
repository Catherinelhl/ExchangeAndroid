package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/17
 * 界面与数据相互作用的协议类：取得账户所有币种信息
 */
public interface GetAllBalanceContract {

    interface View extends BaseContract.View {
        //获取所有币种余额成功
        void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList);

        //获取所有币种余额失败
        void getAllBalanceFailure(String info);

    }

    interface Presenter extends BaseContract.Presenter {
        void getAllBalance();

    }
}
