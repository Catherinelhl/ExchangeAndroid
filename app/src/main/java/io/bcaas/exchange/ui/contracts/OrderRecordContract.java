package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * <p>
 * 订单记录
 */
public interface OrderRecordContract {

    interface View extends BaseContract {
        void getRecordFailure(String info);

        void getRecordSuccess(PaginationVO paginationVO);

        void cancelOrderFailure(String info);
        void cancelOrderSuccess(MemberOrderVO memberOrderVO);
    }

    interface Presenter {
        void getRecord(int type, String nextObjectId);

        // 撤销订单
        void cancelOrder(long memberOrderUid);
    }
}
