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
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
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
    private MemberKeyVO memberKeyVO;
    //记录下当前需要过滤的数据游标，用于下一次匹配是否是同样的数据
    private MemberKeyVO memberKeyVOFilterCursor;

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
        initAdapter();
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
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.SIDE_SLIP_RESET);
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

    private void initAdapter() {
        sidesSlipAdapter = new SidesSlipAdapter(context);
        sidesSlipAdapter.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public <T> void onItemSelect(T type, String from) {
                memberKeyVO = (MemberKeyVO) type;

            }
        });
        gvMethods.setAdapter(sidesSlipAdapter);
    }

    /**
     * @param memberKeyVOFilterCursor 需要过滤掉的数据
     */
    public void setData(MemberKeyVO memberKeyVOFilterCursor) {
        //1：获取当前的所有的币种信息
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
        LogTool.d(TAG, "memberKeyVOList:" + GsonTool.string(memberKeyVOList));
        if (ListTool.isEmpty(memberKeyVOList)) {
            return;
        }
        //用来存储待会过滤出来的新数组
        List<MemberKeyVO> memberKeyVOAfterFilter = new ArrayList<>();
        //2:判断当前用于过滤的游标数据是否为空，否则不执行逻辑比较
        if (memberKeyVOFilterCursor != null) {
            //3:判断是否有历史过滤数据
            if (this.memberKeyVOFilterCursor != null) {
                //4：判断是否是同一条数据
                if (!this.memberKeyVOFilterCursor.equals(memberKeyVOFilterCursor)) {
                    //刷新adapter的数据，并且存储最新游标数据
                    this.memberKeyVOFilterCursor = memberKeyVOFilterCursor;
                    if (sidesSlipAdapter != null) {
                        sidesSlipAdapter.resetData();
                    }
                }
            } else {
                this.memberKeyVOFilterCursor = memberKeyVOFilterCursor;
            }
            //5：比较当前的数据，然后过滤掉当前显示的token type
            for (MemberKeyVO memberKeyVOTemp : memberKeyVOList) {
                if (memberKeyVOTemp != null) {
                    CurrencyListVO currencyListVO = memberKeyVOTemp.getCurrencyListVO();
                    CurrencyListVO currencyListVOFilter = memberKeyVOFilterCursor.getCurrencyListVO();
                    if (currencyListVO != null && currencyListVOFilter != null) {
                        //6：比对两者的UID是否一样，如果一样，就需要过滤掉这个数据,否则添加进新数组里面
                        if (!StringTool.equals(currencyListVO.getCurrencyUid(),
                                currencyListVOFilter.getCurrencyUid())) {
                            memberKeyVOAfterFilter.add(memberKeyVOTemp);

                        }
                    }
                }
            }
        } else {
            //2 else:将所有数据进行显示
            memberKeyVOAfterFilter.addAll(memberKeyVOList);
        }
        if (sidesSlipAdapter != null) {
            sidesSlipAdapter.addList(memberKeyVOAfterFilter);
        }
    }

}
