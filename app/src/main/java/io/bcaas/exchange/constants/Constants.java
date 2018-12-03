package io.bcaas.exchange.constants;

import static io.bcaas.exchange.constants.Constants.KeyMaps.HTTP_COLON;
import static io.bcaas.exchange.constants.Constants.KeyMaps.HTTP_PREFIX;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 */
public class Constants {

    public static final String LOCAL_DEFAULT_IP = "";

    public class ValueMaps {

        public static final int sleepTime100 = 100;
        public static final int sleepTime200 = 200;
        public static final int sleepTime300 = 300;
        public static final int sleepTime400 = 400;
        public static final int sleepTime500 = 500;
        public static final int sleepTime800 = 800;
        public static final int sleepTime1000 = 1000;
        public static final int sleepTime1500 = 1500;
        public static final int sleepTime2000 = 2000;
        public static final int sleepTime3000 = 3000;
        public static final int sleepTime4000 = 4000;
        public static final int sleepTime5000 = 5000;
        public static final int sleepTime10000 = 10000;
        public static final int sleepTime20000 = 20000;
        public static final int sleepTime30000 = 30000;
        public static final int sleepTime50000 = 50000;
        public static final int sleepTime500000 = 500000;

        public static final int TIME_OUT_TIME_LONG = 30;//设置超时时间

    }

    public static String SPLICE_CONVERTER(String ip, int port) {
        return HTTP_PREFIX + ip + HTTP_COLON + port;
    }

    public class KeyMaps {
        public static final String HTTP_PREFIX = "http://";
        public static final String HTTP_COLON = ":";

    }

    public static class ServerType {
        public static final String INTERNATIONAL_SIT = "internationalSIT";
        public static final String INTERNATIONAL_UAT = "internationalUAT";
        public static final String INTERNATIONAL_PRD = "internationalPRD";
        public static final String CHINA = "china";
        public static final String CHINA_SIT = "chinaSIT";

    }

    public static class ServerTypeName {
        public static final String INTERNATIONAL_SIT = "国际 SIT";
        public static final String INTERNATIONAL_UAT = "国际 UAT";
        public static final String INTERNATIONAL_PRD = "国际 PRD";
        public static final String CHINA = "国内 PRD";
        public static final String CHINA_SIT = "国内 SIT";

    }


    public class Preference {
        public static final String SP_BCAAS_EXCHANGE = "BCAAS_EXCHANGE";
    }
}
