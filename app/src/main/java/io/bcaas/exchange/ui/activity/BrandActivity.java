package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import io.bcaas.exchange.BuildConfig;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.ObservableTimerListener;
import io.bcaas.exchange.tools.ObservableTimerTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/18
 * <p>
 * 引导页面
 */
public class BrandActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_brand;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ObservableTimerTool.countDownTimerBySetTime(Constants.ValueMaps.STAY_BRAND_ACTIVITY_TIME, observableTimerListener);

    }


    private ObservableTimerListener observableTimerListener = new ObservableTimerListener() {
        @Override
        public void timeUp(Constants.TimerType timerType) {
            if (BuildConfig.DEBUG) {
                intentToActivity(MainActivity.class, true);
            } else {
                intentToActivity(LoginActivity.class, true);

            }
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
