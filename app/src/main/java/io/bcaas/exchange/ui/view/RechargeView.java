package io.bcaas.exchange.ui.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding2.view.RxView;
import com.obt.qrcode.encoding.EncodingUtils;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「充值」页面视图
 */
public class RechargeView extends LinearLayout {
    ImageView ivQrCode;
    TextView tvMyAddress;
    TextView tvCopyAddress;
    TextView tvInfo;
    TextView tvSetImmediately;

    private Context context;
    private OnItemSelectListener onItemSelectListener;
    String info = "请勿将ETH/ZBA发送至您的比特币(BTC)地址,否则资金将会遗失。比特币的交易需要六个区块的确认,可能会花费1个小时以上才能完成。";

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public RechargeView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public RechargeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recharge, this, true);
        ivQrCode = view.findViewById(R.id.iv_qr_code);
        tvMyAddress = view.findViewById(R.id.tv_my_address);
        tvCopyAddress = view.findViewById(R.id.tv_copy_address);
        tvInfo = view.findViewById(R.id.tv_info);
        tvSetImmediately = view.findViewById(R.id.tv_set_immediately);
        initListener();

    }

    private void initListener() {
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
                    tvInfo.setText(info);
                }
            }
        } else {
            if (tvInfo != null) {
                tvInfo.setText(context.getResources().getString(R.string.no_fund_password_tips));
            }
        }

    }
}
