package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/25
 * 定义一个数据类来存储当前界面的nexPObjectId信息
 */
public class NextObjectIdBean implements Serializable {
    private String uid;
    private String nextObjectId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNextObjectId() {
        return nextObjectId;
    }

    public void setNextObjectId(String nextObjectId) {
        this.nextObjectId = nextObjectId;
    }
}
