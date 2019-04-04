package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * <p>
 * 界面与数据相互作用的协议类：订单记录
 */
public interface OrderRecordContract {

    interface View extends BaseContract.View {
        void getRecordFailure(String info, boolean isRefresh);

        void getRecordSuccess(PaginationVO paginationVO, boolean isRefresh,int responseType);

        void cancelOrderFailure(String info);

        void cancelOrderSuccess(MemberOrderVO memberOrderVO);
    }

    interface Presenter extends BaseContract.Presenter{
        void getRecord(int type, String nextObjectId);

        // 撤销订单
        void cancelOrder(long memberOrderUid);
    }
}
