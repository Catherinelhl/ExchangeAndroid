package io.bcaas.exchange.gson;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    public static int getInt(String resource, String key, int value) {
        if (StringTool.isEmpty(resource)) {
            return value;
        } else if (StringTool.isEmpty(key)) {
            return value;
        } else {
            JSONObject jsonObject = null;
            try {
                JSONArray jsonArray = new JSONArray(resource);
                if (jsonArray.length() ==0){
                    return value;
                }
                jsonObject = jsonArray.getJSONObject(0);
                return !jsonObject.has(key) ? value : jsonObject.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return value;
            }
        }
    }

    public static CurrencyListVO getNextCurrency(String enName) {
        CurrencyListVO currencyListVO = new CurrencyListVO();
        //拿到所有的币种信息
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
        if (ListTool.isEmpty(memberKeyVOList)) {
            return currencyListVO;
        }
        for (int i = 0; i < memberKeyVOList.size(); i++) {
            MemberKeyVO memberKeyVO = memberKeyVOList.get(i);
            if (memberKeyVO == null) {
                continue;
            }
            currencyListVO = memberKeyVO.getCurrencyListVO();
            if (currencyListVO == null) {
                continue;
            }
            String enNameNew = currencyListVO.getEnName();
            if (!StringTool.equals(enNameNew, enName)) {
                break;
            }

        }
        return currencyListVO;
    }
}
