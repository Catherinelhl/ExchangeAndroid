package io.bcaas.exchange.ui.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import com.obt.qrcode.encoding.EncodingUtils;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.UserInfoBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
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

    private Context context;

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
        initListener();

    }

    private void initListener() {
        RxView.clicks(tvCopyAddress).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
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
    }

    /**
     * 更新当前界面信息
     *
     * @param userInfoBean
     */
    public void refreshData(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            String address = userInfoBean.getAddress();
            if (tvMyAddress != null) {
                tvMyAddress.setText(address);
            }

            if (ivQrCode != null) {
                Bitmap qrCode = EncodingUtils.createQRCode(address, context.getResources().getDimensionPixelOffset(R.dimen.d200),
                        context.getResources().getDimensionPixelOffset(R.dimen.d200), null, Constants.ValueMaps.foregroundColorOfQRCode, Constants.ValueMaps.backgroundColorOfQRCode);
                ivQrCode.setImageBitmap(qrCode);
            }

            if (tvInfo != null) {
                tvInfo.setText(userInfoBean.getTips());
            }
        }
    }
}
