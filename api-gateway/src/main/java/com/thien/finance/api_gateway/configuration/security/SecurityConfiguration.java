package com.thien.finance.api_gateway.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        ServerHttpSecurity httpSecurity = http.authorizeExchange(exchanges -> {
            
            // Allow user registration api endpoint
            exchanges.pathMatchers("/identity/api/**").permitAll();
            
            // exchanges.pathMatchers("/identity/api/v1/bank-users/refresh-token").permitAll();
            // exchanges.pathMatchers("/identity/api/v1/bank-users/logout").permitAll();
            // exchanges.pathMatchers("/identity/api/v1/bank-users/sign-up").permitAll();


            // // Allow actuator endpoints
            exchanges.pathMatchers("/actuator/**").permitAll()
                    .pathMatchers("/identity/actuator/**").permitAll()
                    .pathMatchers("/fund-transfer/actuator/**").permitAll()
                    .pathMatchers("/core-banking/actuator/**").permitAll()
                    .pathMatchers("/utility-payment/actuator/**").permitAll()

                    // All Other APIS are authenticated
                    .anyExchange().authenticated();
        });

        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);


        return http.build();
    }
}
