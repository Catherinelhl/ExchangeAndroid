package io.bcaas.exchange.view.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.SidesSlipAdapter;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    private MemberKeyVO memberKeyVO;

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
        view = LayoutInflater.from(context).inflate(R.layout.pop_side_slip, null);
        // 把View添加到PopWindow中
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽1:3.5
        int width = BaseApplication.getScreenWidth() / 4 * 3;
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
        RxView.clicks(tvReset).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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
        RxView.clicks(tvSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.SIDE_SLIP);
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

    public void setData() {
        //获取当前的币种
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
        LogTool.d(TAG, "memberKeyVOList:" + GsonTool.string(memberKeyVOList));
        if (ListTool.isEmpty(memberKeyVOList)) {
            return;
        }
        if (sidesSlipAdapter == null) {
            sidesSlipAdapter = new SidesSlipAdapter(context, memberKeyVOList);
            sidesSlipAdapter.setOnItemSelectListener(new OnItemSelectListener() {
                @Override
                public <T> void onItemSelect(T type, String from) {
                    memberKeyVO = (MemberKeyVO) type;

                }
            });
            gvMethods.setAdapter(sidesSlipAdapter);
        } else {
            sidesSlipAdapter.notifyDataSetChanged();
        }

    }

}
