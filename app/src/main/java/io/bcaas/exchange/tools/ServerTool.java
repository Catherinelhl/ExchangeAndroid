package io.bcaas.exchange.tools;

import io.bcaas.exchange.bean.ServerBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.SystemConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.vo.SeedFullNodeVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/9/27
 * 工具類：服务器数据管理
 */
public class ServerTool {
    private static String TAG = ServerTool.class.getSimpleName();
    /*存储当前可用的SFN*/
    public static List<ServerBean> SFNServerBeanList = new ArrayList<>();
    /*是否需要复活所有服务器*/
    public static boolean needResetServerStatus;
    /*当前默认的服务器*/
    private static ServerBean defaultServerBean;

    public static String getServerType() {
        return SystemConstants.serverType;
    }

    public static void setServerType(String serverType) {
        SystemConstants.serverType = serverType;
    }

    /**
     * 添加国际版SIT服务器（开发）
     */
    public static List<ServerBean> addInternationalSTIServers() {
        List<ServerBean> serverBeans = new ArrayList<>();

        serverBeans.add(getServerBean(serverBeans.size(),
                SystemConstants.EXCHANGE_APP_SIT,
                SystemConstants.EXCHANGE_APP_SIT));
        return serverBeans;

    }


    /**
     * 添加国际版UAT服务器（开发）
     */
    public static List<ServerBean> addInternationalUATServers() {
        List<ServerBean> SFNServerBeanDefaultList = new ArrayList<>();
        return SFNServerBeanDefaultList;
    }

    /**
     * 添加国际版PRO服务器(正式)
     */
    public static List<ServerBean> addInternationalPRDServers() {
        List<ServerBean> SFNServerBeanDefaultList = new ArrayList<>();
        return SFNServerBeanDefaultList;

    }


    private static ServerBean getServerBean(int id, String api, String update) {
        ServerBean serverBean = new ServerBean();
        serverBean.setApiServer(api);
        serverBean.setUpdateServer(update);
        serverBean.setId(id);
        return serverBean;
    }

    private static ServerBean getServerBean(int id, String api) {
        ServerBean serverBean = new ServerBean();
        serverBean.setApiServer(api);
        serverBean.setId(id);
        return serverBean;
    }

    /**
     * 初始化默认的服务器
     */
    public static void initServerData() {
        // 1：为了数据添加不重复，先清理到所有的数据
        cleanServerInfo();
        //2：判断当前的服务器类型，根据标注的服务器类型添加相对应的服务器数据
        switch (getServerType()) {
            //3：添加所有的服务器至全局通用的服务器遍历数组里面进行stand by
            case Constants.ServerType.INTERNATIONAL_SIT:
                SFNServerBeanList.addAll(addInternationalSTIServers());
                break;
            default:
                break;
        }
        GsonTool.logInfo(TAG, getServerType(), SFNServerBeanList);
    }

    //清除所有的服务器信息
    public static void cleanServerInfo() {
        setDefaultServerBean(null);
        SFNServerBeanList.clear();
    }

    /**
     * 添加服务器返回的节点信息
     *
     * @param seedFullNodeBeanListFromServer
     */
    public static void addServerInfo(List<SeedFullNodeVO> seedFullNodeBeanListFromServer) {
        //1：添加默认的服务器数据
        initServerData();
        //2：：添加服务器返回的数据
        if (ListTool.noEmpty(SFNServerBeanList)) {
            //3：遍历服务器返回的数据
            for (int position = 0; position < seedFullNodeBeanListFromServer.size(); position++) {
                //4:与本地默认的作比较
                for (ServerBean serverBeanLocal : SFNServerBeanList) {
                    //5:得到服务端传回的单条数据
                    String apiURL = Constants.SPLICE_CONVERTER(seedFullNodeBeanListFromServer.get(position).getIp(), seedFullNodeBeanListFromServer.get(position).getPort());
                    if (StringTool.equals(apiURL, serverBeanLocal.getApiServer())) {
                        //如果遇到相同的url,则直接break当前循环，开始下一条判断
                        break;
                    }
                    //6:否则添加至当前所有的可请求的服务器存档
                    ServerBean serverBeanNew = new ServerBean(SFNServerBeanList.size(), apiURL, false);
                    //7：通过接口返回的数据没有API和Update接口的domain，所以直接添加当前默认的接口
                    ServerBean serverBean = getDefaultServerBean();
                    if (serverBean != null) {
                        serverBeanNew.setApiServer(serverBean.getApiServer());
                        serverBeanNew.setUpdateServer(serverBean.getUpdateServer());
                    }
                    SFNServerBeanList.add(serverBeanNew);
                    break;
                }
            }

        }
    }

    /**
     * 更换服务器,查看是否有可更换的服务器信息
     */
    public static ServerBean checkAvailableServerToSwitch() {
        //1：取到当前默认的服务器
        ServerBean serverBeanDefault = getDefaultServerBean();
        //2：判断数据非空
        if (serverBeanDefault == null) {
            return null;
        }
        //3：得到当前服务器的id：表示當前服務器的顺序
        int currentServerPosition = serverBeanDefault.getId();
        //4：新建变量用于得到当前需要切换的新服务器
        ServerBean serverBeanNext = null;
        //5：判断当前服务器的id，如果当前id>=0且小于当前数据的数据，代表属于数组里面的数据
        if (currentServerPosition < SFNServerBeanList.size() && currentServerPosition >= 0) {
            ServerBean currentServerBean = SFNServerBeanList.get(currentServerPosition);
            if (currentServerBean != null) {
                currentServerBean.setUnAvailable(true);
            }
            //6：如果当前id小于等于SFNServerBeanList数量-1，代表还有可取的数据
            if (currentServerPosition < SFNServerBeanList.size() - 1) {
                //7:得到新的请求地址信息
                ServerBean serverBeanNew = SFNServerBeanList.get(currentServerPosition + 1);
                if (serverBeanNew != null) {
                    //8：判断新取到的服务器是否可用
                    if (!serverBeanNew.isUnAvailable()) {
                        serverBeanNext = serverBeanNew;
                    }

                }
            } else {
                //否则，检测当前是否需要重置所有服务器的状态
                if (ServerTool.needResetServerStatus) {
                    //否则遍历其中可用的url
                    for (ServerBean serverBean : SFNServerBeanList) {
                        serverBean.setUnAvailable(false);
                    }
                    ServerTool.needResetServerStatus = false;
                }
                //重新选取可用的服务器数据
                for (ServerBean serverBean : SFNServerBeanList) {
                    if (!serverBean.isUnAvailable()) {
                        serverBeanNext = serverBean;
                        break;
                    }
                }
            }

        }
        if (serverBeanNext != null) {
            setDefaultServerBean(serverBeanNext);
            return serverBeanNext;
        }
        return null;
    }

    public static ServerBean getDefaultServerBean() {
        if (defaultServerBean == null) {
            //设置默认的服务器
            setDefaultServerBean(SFNServerBeanList.get(0));
        }
        return defaultServerBean;
    }

    public static void setDefaultServerBean(ServerBean defaultServerBean) {
        ServerTool.defaultServerBean = defaultServerBean;
    }
}
