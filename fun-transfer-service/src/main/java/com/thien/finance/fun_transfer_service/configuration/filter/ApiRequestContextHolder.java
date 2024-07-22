package com.thien.finance.fun_transfer_service.configuration.filter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ApiRequestContextHolder {

    private ApiRequestContextHolder() {
    }

    private static final ThreadLocal<ApiRequestContext> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static ApiRequestContext getContext() {

        ApiRequestContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = new ApiRequestContext();
            contextHolder.set(ctx);
            log.debug("getContext() : new APIRequestContext created..!");
        }

        return ctx;
    }

}
