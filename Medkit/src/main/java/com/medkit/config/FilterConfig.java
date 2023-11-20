package com.medkit.config;

import com.medkit.filter.TokenFilter;
import com.medkit.filter.AuthKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilter() {
        FilterRegistrationBean<TokenFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new TokenFilter());
        bean.addUrlPatterns("/app/*");

        return bean;
    }

    @Bean
    public  FilterRegistrationBean<AuthKeyFilter> authKeyFilter() {
        FilterRegistrationBean<AuthKeyFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new AuthKeyFilter());
        bean.addUrlPatterns("*");
        bean.setOrder(0);

        return bean;
    }
}
