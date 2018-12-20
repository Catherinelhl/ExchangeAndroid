package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;
import io.bcaas.exchange.ui.activity.SellDetailActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 賣出
 */
public class SellFragment extends BaseFragment {
    private String TAG = SellFragment.class.getSimpleName();

    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_sell_volume)
    TextView tvSellVolume;
    @BindView(R.id.tv_progress_speed)
    TextView tvProgressSpeed;
    @BindView(R.id.tv_fee_introduction)
    TextView tvFeeIntroduction;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.btn_sell)
    Button btnSell;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;

    @Override
    protected void onUserVisible() {
        LogTool.i(TAG, "onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG, "onUserInvisible");

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sell;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (tvProgressSpeed != null) {
                    float seekBarWidth = seekBar.getWidth();//seekBar的宽度
                    int margin = getResources().getDimensionPixelSize(R.dimen.text_size_20);
                    float width = (seekBarWidth - margin * 2) / 100 * progress; //seekBar当前位置的宽度
                    tvProgressSpeed.setX(width + margin);
                    tvProgressSpeed.setText(String.valueOf(progress));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        RxView.clicks(btnSell).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Intent intent = new Intent();
//                        Bundle bundle=new Bundle();
//                        bundle.putSerializable(Constants.KeyMaps.BUY_DETAIL,buyDataBean);
//                        intent.putExtras(bundle);
                        intent.setClass(activity, SellDetailActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.SELL_DETAIL_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
    }

}
