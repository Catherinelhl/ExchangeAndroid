package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.ObservableTimerListener;
import io.bcaas.exchange.tools.time.ObservableTimerTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/18
 * <p>
 * Activity：引导页面
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
        BaseApplication.setCurrentLanguage(getCurrentLanguage());
        ObservableTimerTool.countDownTimerBySetTime(Constants.Time.STAY_BRAND_ACTIVITY, observableTimerListener);
    }


    private ObservableTimerListener observableTimerListener = new ObservableTimerListener() {
        @Override
        public void timeUp(Constants.TimerType timerType) {
            // TODO: 2019/1/17 判断当前是否有token存在进行不同页面的跳转
//            if (BuildConfig.DEBUG) {
//                intentToActivity(GetCoinMarketCapActivity.class, true);
//                intentToActivity(MainActivity.class, true);
//            } else {
//            intentToActivity(LoginActivity.class, true);
            intentToActivity(MainActivity.class, true);
//            }
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
