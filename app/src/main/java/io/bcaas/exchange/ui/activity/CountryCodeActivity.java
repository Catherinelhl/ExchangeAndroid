package io.bcaas.exchange.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.CountryCodeAdapter;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/9
 */
public class CountryCodeActivity extends BaseActivity {
    private String TAG = CountryCodeActivity.class.getSimpleName();
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    private CountryCodeAdapter adapter;
    private List<CountryCodeBean.CountryCode> countryCodeList;

    @Override
    public int getContentView() {
        return R.layout.activity_country_code;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        String content = bundle.getString(Constants.From.COUNTRY_CODE);
        if (StringTool.notEmpty(content)) {
            countryCodeList = GsonTool.convert(content, new TypeToken<List<CountryCodeBean.CountryCode>>() {
            }.getType());
        }

    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("请选择地区号");
        ibBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        initAdapter();
    }

    private void initAdapter() {
        adapter = new CountryCodeAdapter(context, countryCodeList);
        adapter.setOnItemSelectListener(onItemSelectListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void initListener() {
        RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (type != null) {
                if (type instanceof CountryCodeBean.CountryCode) {
                    setResult((CountryCodeBean.CountryCode) type);
                }
            }
        }
    };

    public void setResult(CountryCodeBean.CountryCode countryCode) {
        hideSoftKeyboard();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KeyMaps.SELECT_COUNTRY_CODE, countryCode);
        intent.putExtras(bundle);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

}
