package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 *  ModifyPasswordContractModifyPasswordContract
 */
public interface ModifyPasswordContract {

    interface View extends BaseContract.View{
        void resetPasswordFailure(String info);

        void resetPasswordSuccess(String info);
    }

    interface Presenter {
        void resetPassword(String password, String newPassword);
    }
}
