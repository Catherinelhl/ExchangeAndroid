package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.PaginationVO;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 *  界面与数据相互作用的协议类：待出售
 */
public interface ForSaleOrderListContract {

    interface View extends BaseContract.View {
        void getOrderListSuccess(PaginationVO paginationVO);

        void getOrderListFailure(String info);
    }

    interface Presenter {
        void getOrderList(String currencyUid, String currencyPaymentUid, String nextObjectId);
    }
}
