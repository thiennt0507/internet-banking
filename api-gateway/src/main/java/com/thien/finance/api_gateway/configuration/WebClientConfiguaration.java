package com.thien.finance.api_gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.thien.finance.api_gateway.repository.IdentityClient;

@Configuration
public class WebClientConfiguaration {
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8089")
                .build();
    }

    @Bean
    IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }
}
