package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 购买详情
 */
public class SellDetailActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_purple_title)
    TextView tvPurpleTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etwaFundPassword;
    @BindView(R.id.etwa_random_verify_code)
    EditTextWithAction etwaRandomVerifyCode;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.btn_sell)
    Button btnSell;
    private BuyDataBean buyDataBean;

    @Override
    public int getContentView() {
        return R.layout.activity_sell_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        buyDataBean = (BuyDataBean) bundle.getSerializable(Constants.KeyMaps.BUY_DETAIL);
        LogTool.d(TAG, buyDataBean);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.buy_detail);
        if (buyDataBean != null) {
            tvPrice.setText(buyDataBean.getPrice());
            tvNumber.setText(buyDataBean.getNumber());
            tvFee.setText(buyDataBean.getFee());
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        RxView.clicks(ibBack).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        setResult(true);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnSell).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
                        setResult(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
