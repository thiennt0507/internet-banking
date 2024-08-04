package com.thien.finance.api_gateway.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        ServerHttpSecurity httpSecurity = http;
        // .authorizeExchange(exchanges -> {
            
        //     // Allow user registration api endpoint
        //     exchanges.pathMatchers("/identity/api/**").permitAll();
        //     // exchanges.pathMatchers("/**").permitAll();


        //     // // Allow actuator endpoints
        //     exchanges.pathMatchers("/actuator/**").permitAll()
        //             .pathMatchers("/identity/actuator/**").permitAll()
        //             .pathMatchers("/fund-transfer/actuator/**").permitAll()
        //             .pathMatchers("/core-banking/actuator/**").permitAll()
        //             .pathMatchers("/utility-payment/actuator/**").permitAll()
        //             .anyExchange().authenticated();
        // });

    
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

    // @Bean
    // public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
    //     http
    //         .httpBasic(httpBasic -> httpBasic.disable())
    //         .formLogin(formLogin -> formLogin.disable())
    //         .csrf(csrf -> csrf.disable())
    //         .logout(logout -> logout.disable());
    //     http
    //         .exceptionHandling(exceptionHandlingSpec ->
    //                 exceptionHandlingSpec.authenticationEntryPoint((swe, e) ->
    //                 Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
    //         ).accessDeniedHandler((swe, e) ->
    //                 Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
    //         ))
    //         .authenticationManager(authenticationManager)
    //         .securityContextRepository(securityContextRepository)
    //         .authorizeExchange(authorizeExchangeSpec ->
    //                 authorizeExchangeSpec
    //                 .pathMatchers(HttpMethod.OPTIONS).permitAll()
    //                 .pathMatchers("/identity/api/**").permitAll()
    //                 // .pathMatchers("/**").permitAll()
    //                 .anyExchange().authenticated());

    //     return http.build();
    // }
}
