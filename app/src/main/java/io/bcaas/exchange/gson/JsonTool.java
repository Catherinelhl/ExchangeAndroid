package io.bcaas.exchange.gson;

import io.bcaas.exchange.tools.StringTool;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * BcaasAndroid
 * <p>
 * io.bcaasc.tools
 * <p>
 * created by catherine in 九月/04/2018/下午5:20
 * 工具類：JSON 数据判断
 */
public class JsonTool {

    private static String TAG = JsonTool.class.getSimpleName();

    public static String getString(String resource, String key) {
        return getString(resource, key, (String) null);
    }

    public static String getString(String resource, String key, String value) {
        if (StringTool.isEmpty(resource)) {
            return value;
        } else if (StringTool.isEmpty(key)) {
            return value;
        } else {
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(resource);
                return !jsonObject.has(key) ? value : jsonObject.getString(key);
            } catch (JSONException var5) {
                return value;
            }
        }
    }

    public static int getInt(String resource, String key,int value) {
        if (StringTool.isEmpty(resource)) {
            return value;
        } else if (StringTool.isEmpty(key)) {
            return value;
        } else {
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(resource);
                return !jsonObject.has(key) ? value : jsonObject.getInt(key);
            } catch (JSONException var5) {
                return value;
            }
        }
    }
}
