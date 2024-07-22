package com.thien.finance.fun_transfer_service.configuration.audit;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.thien.finance.fun_transfer_service.configuration.filter.AppAuthUserFilter;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<String> myAuditorProvider() {
        return new AuditorAwareConfig();
    }

    @Bean
    public FilterRegistrationBean<AppAuthUserFilter> authUserFilter() {
        FilterRegistrationBean<AppAuthUserFilter> registrationBean
            = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AppAuthUserFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

}
