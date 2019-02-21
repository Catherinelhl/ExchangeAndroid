package io.bcaas.exchange.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import io.bcaas.exchange.listener.FilterListener;
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
 * Activity：城市区号展示
 */
public class CountryCodeActivity extends BaseActivity {
    private String TAG = CountryCodeActivity.class.getSimpleName();


    @BindView(R.id.ib_search)
    ImageButton ibSearch;
    @BindView(R.id.et_select_content)
    EditText etSelectContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_country_code)
    LinearLayout llCountryCode;
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
    }

    @Override
    public void initData() {
        initAdapter();
    }

    private void initAdapter() {
        adapter = new CountryCodeAdapter(context, countryCodeList);
        adapter.setOnItemSelectListener(onItemSelectListener);
        adapter.setFilterListener(filterListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void initListener() {
        hideSoftKeyBoardByTouchView(llCountryCode);
        RxView.clicks(tvCancel).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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

        /**
         * 对编辑框添加文本改变监听，搜索的具体功能在这里实现
         * 很简单，文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。
         */
        etSelectContent.addTextChangedListener(new TextWatcher() {

            /**
             *
             * 编辑框内容改变的时候会执行该方法
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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

    private FilterListener filterListener = new FilterListener() {
        @Override
        public void getFilterData(List<CountryCodeBean.CountryCode> countryCodes) {
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
