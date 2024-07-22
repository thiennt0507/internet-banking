package com.thien.finance.fun_transfer_service.configuration.audit;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.AuditorAware;

import com.thien.finance.fun_transfer_service.configuration.filter.ApiRequestContextHolder;

import java.util.Optional;

public class AuditorAwareConfig implements AuditorAware<String> {
    @Override public Optional<String> getCurrentAuditor() {
        if (StringUtils.isEmpty(ApiRequestContextHolder.getContext().getAuthId())) {
            return Optional.of("SYSTEM_USER");
        }
        return Optional.of(ApiRequestContextHolder.getContext().getAuthId());
    }
}
