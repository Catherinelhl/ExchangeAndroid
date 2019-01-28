package io.bcaas.exchange.ui.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import com.obt.qrcode.encoding.EncodingUtils;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「充值」页面视图
 */
public class RechargeView extends BaseLinearLayout {
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_set_immediately)
    TextView tvSetImmediately;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_my_address)
    TextView tvMyAddress;
    @BindView(R.id.tv_copy_address)
    TextView tvCopyAddress;

    private Context context;
    private OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public RechargeView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    @Override
    protected int setContentView() {
        return R.layout.view_recharge;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        RxView.clicks(tvCopyAddress).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText(Constants.KeyMaps.COPY_ADDRESS, tvMyAddress.getText());
                        // 将ClipData内容放到系统剪贴板里。
                        if (cm != null) {
                            cm.setPrimaryClip(mClipData);
                            Toast.makeText(context, context.getResources().getString(R.string.successfully_copied), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvSetImmediately).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(MessageConstants.EMPTY, MessageConstants.EMPTY);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 更新当前界面信息
     *
     * @param memberKeyVO
     */
    public void refreshData(MemberKeyVO memberKeyVO) {
        //判断当前是否设置资金密码
        boolean hasFundPassword = BaseApplication.isSetFundPassword();
        if (tvMyAddress != null) {
            tvMyAddress.setVisibility(hasFundPassword ? VISIBLE : GONE);
        }

        if (ivQrCode != null) {
            ivQrCode.setVisibility(hasFundPassword ? VISIBLE : GONE);
        }
        if (tvCopyAddress != null) {
            tvCopyAddress.setVisibility(hasFundPassword ? VISIBLE : GONE);
        }
        if (tvSetImmediately != null) {
            tvSetImmediately.setVisibility(hasFundPassword ? GONE : VISIBLE);
        }
        if (hasFundPassword) {
            if (memberKeyVO != null) {
                String address = memberKeyVO.getAddress();
                if (tvMyAddress != null) {
                    tvMyAddress.setText(address);
                }

                if (ivQrCode != null) {
                    Bitmap qrCode = EncodingUtils.createQRCode(address, context.getResources().getDimensionPixelOffset(R.dimen.d200),
                            context.getResources().getDimensionPixelOffset(R.dimen.d200), null, Constants.Color.foregroundOfQRCode, Constants.Color.backgroundOfQRCode);
                    ivQrCode.setImageBitmap(qrCode);
                }

                if (tvInfo != null) {
                    CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
                    if (currencyListVO != null) {
                        String uid = currencyListVO.getCurrencyUid();
                        if (StringTool.equals(uid, "1")) {//BTC
                            tvInfo.setText(context.getResources().getString(R.string.btc_recharge_info));

                        } else if (StringTool.equals(uid, "2")) {//ETH
                            tvInfo.setText(context.getResources().getString(R.string.eth_recharge_info));

                        } else {//BCC
                            tvInfo.setText(context.getResources().getString(R.string.bcc_recharge_info));
                        }
                    } else {//BCC
                        tvInfo.setText(context.getResources().getString(R.string.bcc_recharge_info));

                    }
                }
            }
        } else {
            if (tvInfo != null) {
                tvInfo.setText(context.getResources().getString(R.string.no_fund_password_tips));
            }
        }

    }
}
