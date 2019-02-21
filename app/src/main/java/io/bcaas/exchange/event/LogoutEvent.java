package io.bcaas.exchange.event;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/17
 * 事件订阅Cursor：提示登出事件
 */
public class LogoutEvent {
   private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
