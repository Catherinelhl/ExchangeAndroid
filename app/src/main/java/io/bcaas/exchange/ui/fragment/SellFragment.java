package io.bcaas.exchange.ui.fragment;

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
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.tools.LogTool;

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
    @BindView(R.id.btn_login)
    Button btnLogin;
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
    }


}
