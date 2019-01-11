package io.bcaas.exchange;

import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.tools.regex.RegexTool;

import java.security.NoSuchAlgorithmException;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/25
 */
public class Test {

    public static void main(String[] args) {
        try {
            //173af653133d964edfc16cafe0aba33c8f500a07f3ba3f81943916910c257057
            //5a77d1e9612d350b3734f6282259b7ff0a3f87d62cfef5f35e91a5604c0490a3
            System.out.println(Sha256Tool.doubleSha256ToString("123"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("this is a test....");
        System.out.println(RegexTool.isRightEmail("2@1.bcaas.in"));
    }
}
