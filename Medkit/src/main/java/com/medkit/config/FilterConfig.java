package com.medkit.config;

import com.medkit.filter.*;
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

    @Bean
    public  FilterRegistrationBean<AdminFilter> adminFilter() {
        FilterRegistrationBean<AdminFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new AdminFilter());
        bean.addUrlPatterns("/app/UserViewer");
        bean.addUrlPatterns("/app/SymptomViewer");
        bean.addUrlPatterns("/app/SymptomEditor");
        bean.addUrlPatterns("/app/AllMedicine");
        bean.addUrlPatterns("/app/MedicineEditor");
        bean.addUrlPatterns("/app/DiseaseViewer");
        bean.addUrlPatterns("/app/DiseaseEditor");
        bean.setOrder(0);

        return bean;
    }

    @Bean
    public  FilterRegistrationBean<DoctorFilter> doctorFilter() {
        FilterRegistrationBean<DoctorFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new DoctorFilter());
        bean.addUrlPatterns("/app/DiagnoseEditor");
        bean.setOrder(0);

        return bean;
    }

    @Bean
    public  FilterRegistrationBean<PatientFilter> patientFilter() {
        FilterRegistrationBean<PatientFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new PatientFilter());
        bean.addUrlPatterns("/app/AppointmentEditor");
        bean.setOrder(0);

        return bean;
    }
}
