package io.bcaas.exchange.tools.file;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/9
 * <p>
 * 工具类：资源管理
 */
public class ResourceTool {

    /**
     * 根据文件名字获取assets下面的json文件
     *
     * @param fileName
     * @return
     */
    public static String getJsonFromAssets(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = BaseApplication.context().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static int getDrawableByEnName(String enName) {
        int res = 0;
        switch (enName) {
            case "BTC":
                res = R.mipmap.icon_coin_btc;
                break;
            case "ETH":
                res = R.mipmap.icon_coin_eth;
                break;
            case "ZBB":
                res = R.mipmap.icon_coin_zbb;
                break;
            case "BCC":
                res = R.mipmap.icon_coin_bcc;
                break;
            case "CNYC":
                res = R.mipmap.icon_coin_cnyc;
                break;
        }
        return res;
    }
}
