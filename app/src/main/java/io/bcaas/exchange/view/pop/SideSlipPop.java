package io.bcaas.exchange.view.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.SidesSlipAdapter;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/4
 * 侧滑界面，这里用的是自定义popWindow来实现
 */
public class SideSlipPop extends PopupWindow {
    private String TAG = SideSlipPop.class.getSimpleName();
    GridView gvMethods;
    TextView tvReset;
    TextView tvSure;

    private Context context;
    private View view;
    //当前的选择
    private String currentSelect;

    private List<String> payMethod;
    private OnItemSelectListener onItemSelectListener;
    private SidesSlipAdapter sidesSlipAdapter;

    public SideSlipPop(Context context) {
        this.context = context;
        initView();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.popwindow_side_slip, null);
        // 把View添加到PopWindow中
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽1:3.5
        int width = BaseApplication.getScreenWidth() / 4 * 3;
        LogTool.d("width:" + width);
        this.setWidth(width);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //  设置SelectPicPopupWindow弹出窗体可点击
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        //   设置背景透明
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gvMethods = view.findViewById(R.id.gv_methods);
        tvReset = view.findViewById(R.id.tv_reset);
        tvSure = view.findViewById(R.id.tv_sure);
        tvReset = view.findViewById(R.id.tv_reset);
        payMethod = new ArrayList<>();
        payMethod.add("BTC");
        payMethod.add("ETH");
        payMethod.add("ZBB");
        setData();
        RxView.clicks(tvReset).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (sidesSlipAdapter != null) {
                            sidesSlipAdapter.resetData();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvSure).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(currentSelect, Constants.From.SIDESLIP);
                        }
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setData() {
        sidesSlipAdapter = new SidesSlipAdapter(context, payMethod);
        sidesSlipAdapter.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public <T> void onItemSelect(T type, String from) {
                currentSelect = (String) type;

            }
        });
        gvMethods.setAdapter(sidesSlipAdapter);

    }
}
