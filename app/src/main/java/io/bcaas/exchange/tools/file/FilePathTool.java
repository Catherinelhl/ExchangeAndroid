package io.bcaas.exchange.tools.file;

import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.StringTool;

import java.io.File;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/9
 * 管理文件路径
 */
public class FilePathTool {

    /**
     * 根据当前约言环境获取相对应的文件
     *
     * @param getCurrentLanguage
     * @return
     */
    public static String getCountryCodeFilePath(String getCurrentLanguage) {
        if (StringTool.equals(getCurrentLanguage, Constants.ValueMaps.ZH_CN)) {
            return Constants.FilePath.COUNTRY_CODE + File.separator + Constants.FilePath.ZH_CN_COUNTRY_CODE;
        } else if (StringTool.equals(getCurrentLanguage, Constants.ValueMaps.ZH_TW)) {
            return Constants.FilePath.COUNTRY_CODE + File.separator + Constants.FilePath.ZH_TW_COUNTRY_CODE;
        } else {
            return Constants.FilePath.COUNTRY_CODE + File.separator + Constants.FilePath.EN_US_COUNTRY_CODE;
        }
    }
}
