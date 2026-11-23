package com.zodiakk.intelligentservice.security.config;

import com.zodiakk.intelligentservice.common.constants.SecurityConstants;
import com.zodiakk.intelligentservice.security.filter.InternalApiKeyFilter;
import com.zodiakk.intelligentservice.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Value("${internal.api-key}") String internalApiKey,
            @Value("${app.openapi.anonymous:false}") boolean openApiAnonymous
    ) throws Exception {
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers(
                            "/actuator/health",
                            "/actuator/health/**",
                            "/actuator/info",
                            "/error"
                    ).permitAll();
                    if (openApiAnonymous) {
                        auth.requestMatchers(
                                "/api-docs",
                                "/api-docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll();
                    }
                    auth.requestMatchers("/internal/**").hasAuthority(SecurityConstants.INTERNAL_SERVICE_AUTHORITY);
                    auth.anyRequest().denyAll();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                .addFilterBefore(new InternalApiKeyFilter(internalApiKey, entryPoint), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
