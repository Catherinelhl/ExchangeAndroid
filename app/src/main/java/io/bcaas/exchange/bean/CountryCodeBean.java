package io.bcaas.exchange.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/9
 *
 * 获取本地地区号码资源
 */
public class CountryCodeBean implements Serializable {
    public List<CountryCode> data;

    public class CountryCode implements Serializable{
        private String countryName;
        private String countryPinyin;
        private String phoneCode;
        private String countryCode;

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryPinyin() {
            return countryPinyin;
        }

        public void setCountryPinyin(String countryPinyin) {
            this.countryPinyin = countryPinyin;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        @Override
        public String toString() {
            return "CountryCode{" +
                    "countryName='" + countryName + '\'' +
                    ", countryPinyin='" + countryPinyin + '\'' +
                    ", phoneCode='" + phoneCode + '\'' +
                    ", countryCode='" + countryCode + '\'' +
                    '}';
        }
    }

    public List<CountryCode> getData() {
        return data;
    }

    public void setData(List<CountryCode> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CountryCodeBean{" +
                "data=" + data +
                '}';
    }
}
