package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/9/17
 * <p>
 * 实体类：存儲當前所在SFN的環境的所有可切換的服務器信息數據類
 */
public class ServerBean implements Serializable {
    /*id，增序,默认为-1*/
    private int id = -1;
    /*服务器 api_url*/
    private String apiServer;
    /*服务器 update_url*/
    private String updateServer;
    /*是否不可用，默认是false，如果在请求超时后，需要将其置为true*/
    private boolean isUnAvailable;

    public ServerBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServerBean(int id, boolean isChoose) {
        super();
        this.id = id;
    }

    public ServerBean(int id, String apiServer,boolean isChoose) {
        super();
        this.id = id;
        this.apiServer=apiServer;
    }

    public ServerBean(int id, String apiServer, String updateServer, boolean isChoose) {
        super();
        this.id = id;
        this.apiServer = apiServer;
        this.updateServer = updateServer;
    }
    public String getApiServer() {
        return apiServer;
    }

    public void setApiServer(String apiServer) {
        this.apiServer = apiServer;
    }

    public String getUpdateServer() {
        return updateServer;
    }

    public void setUpdateServer(String updateServer) {
        this.updateServer = updateServer;
    }


    public boolean isUnAvailable() {
        return isUnAvailable;
    }

    public void setUnAvailable(boolean unAvailable) {
        isUnAvailable = unAvailable;
    }

    @Override
    public String toString() {
        return "ServerBean{" +
                "id=" + id +
                ", apiServer='" + apiServer + '\'' +
                ", updateServer='" + updateServer + '\'' +
                ", isUnAvailable=" + isUnAvailable +
                '}';
    }
}
