package io.bcaas.exchange.listener;

import io.bcaas.exchange.constants.Constants;

/**
 * @author catherine.brainwilliam
 * @since 2018/10/20
 * <p>
 * 回調監聽： 时间倒计时、定时器時間到調用
 */
public interface ObservableTimerListener {
    //时间到
    void timeUp(Constants.TimerType timerType);
}
