package io.bcaas.exchange;

import io.bcaas.exchange.tools.decimal.DecimalTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/25
 */
public class Test {

    enum EnumTest {
        bindPhone(1, "red");

        private int key;

        private String value;

        // 构造函数，枚举类型只能为私有
        EnumTest(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public static String valueOf(int i) {
            for (EnumTest enumTest : values()) {
                if (enumTest.getKey() == i) {
                    return enumTest.getValue();
                }
            }
            return null;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        System.out.println(DecimalTool.calculateFirstSubtractSecondValue("1,999.99999999","1",true));
//        try {
//            //173af653133d964edfc16cafe0aba33c8f500a07f3ba3f81943916910c257057
//            //5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3
//            System.out.println(Sha256Tool.doubleSha256ToString("123"));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        System.out.println("this is a test....");
//        System.out.println(RegexTool.isRightEmail("2@1.bcaas.in"));
        System.out.println(EnumTest.valueOf(0));

//        for (EnumTest enumTest : EnumTest.values()) {
//            switch (enumTest) {
//                case bindPhone:
//
//                    break;
//            }
//            System.out.println(enumTest.name);
//            System.out.println(enumTest.value);
//            System.out.println(enumTest.getName());
//        }
    }
}
