package io.bcaas.exchange.listener;

import io.bcaas.exchange.bean.CountryCodeBean;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/14
 * 过滤器
 */
public interface FilterListener {
    void getFilterData(List<CountryCodeBean.CountryCode> countryCodes);
}
