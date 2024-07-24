package com.thien.finance.identity_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.thien.finance.identity_service.config.jwt.JwtTokenUtils;
import com.thien.finance.identity_service.config.user.UserInfoManagerConfig;

import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;


@Configuration
@EnableWebSecurity
public class AuthConfig {
    @Autowired
    private UserInfoManagerConfig userInfoManagerConfig;
    
    @Autowired
    private  RSAKeyRecord rsaKeyRecord;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    
    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }



    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    //     return http.csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(requests -> {
    //                 requests.requestMatchers("/auth/register", "/auth/token", "/auth/validate").permitAll();
    //                 requests.anyRequest().authenticated();
    //             })
    //             .oauth2Login(withDefaults())
    //             .formLogin(withDefaults())
    //             .build();
    // }

    @Order(1)
    @Bean
    public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userInfoManagerConfig)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                })
                .httpBasic(withDefaults())
                .build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAccessTokenFilter(rsaKeyRecord, jwtTokenUtils), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    log.error("[SecurityConfig:apiSecurityFilterChain] Exception due to :{}",ex);
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .httpBasic(withDefaults())
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
