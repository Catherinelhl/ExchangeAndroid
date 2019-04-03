package io.bcaas.exchange.tools;

import android.text.TextUtils;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.tools.decimal.DecimalTool;
import io.bcaas.exchange.vo.CurrencyListVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/15
 * 工具類：字符串判斷
 */
public class StringTool {
    private static String TAG = StringTool.class.getSimpleName();

    public static boolean isEmpty(String content) {
        return TextUtils.isEmpty(content);
    }

    public static boolean notEmpty(String content) {
        return !isEmpty(content);
    }

    public static boolean equals(String str1, String str2) {
        return TextUtils.equals(str1, str2);
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        if (seq != null && searchSeq != null) {
            return TextUtils.indexOf(seq, searchSeq, 0) >= 0;
        } else {
            return false;
        }

    }

    /**
     * @param str
     * @return 去除不规则空格，平常空格，全角空格
     */
    public static String removeIllegalSpace(String str) {
        if (str == null) {
            return null;
        }
//        char nbsp = 0x00a0;//160
//        char qjnbsp = '\u3000';//12288
//        return str.replace(nbsp, ' ').replace(qjnbsp, ' ');

        char[] newt = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char w : newt) {
            int i = (int) w;
            if (i == 160 || i == 32 || i == 12288) {
                sb.append(" ");
            } else {
                sb.append(String.valueOf(w));
            }
        }
        return sb.toString();
    }

    /**
     * 根据传入的订单类型。返回可以显示的文本信息
     * (0:转入, 1:提現, 2:買, 3:賣)
     *
     * @param type
     * @return
     */
    public static String getDisplayOrderTypeText(int type) {
        if (type == 0) {
            return "转入";
        } else if (type == 1) {
            return "转出";

        } else if (type == 2) {
            return "购买";
        } else if (type == 3) {
            return "出售";
        }
        return "转入";
    }

    /**
     * 訂單種類對應的狀態<br>
     * 0:转入 ---> (0:失敗, 1:已完成)<br>
     * 1:提現 ---> (0:失敗, 1:已完成, 2:待驗證)<br>
     * 2:買 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
     * 3:賣 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
     */
    public static String getDisplayOrderStatusText(int type, int status) {
        switch (type) {
            case 0://转入
                if (status == 0) {
                    return "失敗";
                } else if (status == 1) {
                    return "已完成";
                } else if (status == 2) {
                    return "待验证";
                }
                break;
            case 1://转出
                if (status == 0) {
                    return "失敗";
                } else if (status == 1) {
                    return "已完成";
                } else if (status == 2) {
                    return "待验证";
                }
                break;
            case 2://买
                if (status == 0) {
                    return "已撤銷";
                } else if (status == 1) {
                    return "已完成";
                } else if (status == 2) {
                    return "待出售";
                }

                break;
            case 3://卖
                if (status == 0) {
                    return "已撤銷";
                } else if (status == 1) {
                    return "已完成";
                } else if (status == 2) {
                    return "待出售";
                }

                break;
        }
        return MessageConstants.EMPTY;
    }

    /**
     * 訂單種類對應的狀態<br>
     * 0:转入 ---> (0:失敗, 1:已完成)<br>
     * 1:提現 ---> (0:失敗, 1:已完成, 2:待驗證)<br>
     * 2:買 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
     * 3:賣 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
     */
    public static boolean getDisplayOrderStatus(int type, int status) {
        switch (type) {
            case 0://转入
                if (status == 0) {
                    return false;
                } else if (status == 1) {
                    return false;
                }
                break;
            case 1://转出
                if (status == 0) {
                    return false;
                } else if (status == 1) {
                    return false;
                } else if (status == 2) {
                    return false;
                }
                break;
            case 2://买
                if (status == 0) {
                    return false;
                } else if (status == 1) {
                    return false;
                } else if (status == 2) {
                    return true;
                }

                break;
            case 3://卖
                if (status == 0) {
                    return false;
                } else if (status == 1) {
                    return false;
                } else if (status == 2) {
                    return true;
                }

                break;
        }
        return false;
    }

    /**
     * 根据传入的enName得到CoinName
     *
     * @param enName
     * @return
     */
    public static String getCoinNameFromCurrencyList(String enName) {
        //1:取得当前账户里面的currencyListWithCoinName信息
        List<CurrencyListVO> currencyListVOList = BaseApplication.getCurrencyListVOSWithCoinName();
        if (ListTool.isEmpty(currencyListVOList)) {
            return MessageConstants.EMPTY;
        }
        for (CurrencyListVO currencyListVO : currencyListVOList) {
            if (currencyListVO != null) {
                if (StringTool.equals(currencyListVO.getEnName(), enName)) {
                    return currencyListVO.getCoinName();
                }
            }
        }
        return MessageConstants.EMPTY;
    }

    /**
     * 根据传入的UID来决定显示文本的精度
     *
     * @param balanceAvailable
     * @param uid
     * @return
     */
    public static String getDisplayAmountByUId(String balanceAvailable, String uid) {
        if (StringTool.notEmpty(uid)) {
            switch (uid) {
                case "0"://BCC
                    return DecimalTool.transferDisplay(8, balanceAvailable, Constants.Pattern.EIGHT_DISPLAY);
                case "1"://BTC
                    return DecimalTool.transferDisplay(8, balanceAvailable, Constants.Pattern.EIGHT_DISPLAY);
                case "3"://CNYC
                    return DecimalTool.transferDisplay(10, balanceAvailable, Constants.Pattern.TEN_DISPLAY);
                case "2"://ETH
                    return DecimalTool.transferDisplay(10, balanceAvailable, Constants.Pattern.TEN_DISPLAY);
            }
        }
        return balanceAvailable;
    }

    /**
     * 根据传入的UID来决定逻辑处理的数据格式
     *
     * @param balanceAvailable
     * @param uid
     * @return
     */
    public static String getTransferStoreDatabaseAmountByUId(String balanceAvailable, String uid) {
        if (StringTool.notEmpty(uid)) {
            switch (uid) {
                case "0"://BCC
                    return DecimalTool.transferStoreDatabase(8, balanceAvailable, Constants.Pattern.EIGHT);
                case "1"://BTC
                    return DecimalTool.transferStoreDatabase(8, balanceAvailable, Constants.Pattern.EIGHT);
                case "2"://ETH
                    return DecimalTool.transferStoreDatabase(10, balanceAvailable, Constants.Pattern.TEN);
                case "3"://CNYC
                    return DecimalTool.transferStoreDatabase(10, balanceAvailable, Constants.Pattern.TEN);
            }
        }
        return balanceAvailable;
    }

    public static String getMinValuesByUid(String uid) {
        if (StringTool.notEmpty(uid)) {
            switch (uid) {
                case "0"://BCC
                    return Constants.DigitalPrecision.BCC;
                case "1"://BTC
                    return Constants.DigitalPrecision.BTC;
                case "2"://ETH
                    return Constants.DigitalPrecision.ETH;
                case "3"://CNYC
                    return Constants.DigitalPrecision.CNYC;
            }
        }
        return Constants.DigitalPrecision.DEFAULT;
    }

    /**
     * 根据传入的uid，得到相对应的保留精度
     *
     * @param currencyUid
     * @return
     */
    public static int getDigitsNumber(String currencyUid) {
        if (StringTool.isEmpty(currencyUid)) {
            return Constants.DigitalPrecision.LIMIT_EIGHT;
        }
        switch (currencyUid) {
            case "0"://BCC
            case "1"://BTC
                return Constants.DigitalPrecision.LIMIT_EIGHT;
            case "2"://ETH
            case "3"://CNYC
                return Constants.DigitalPrecision.LIMIT_TEN;
        }
        return Constants.DigitalPrecision.LIMIT_EIGHT;
    }
}
