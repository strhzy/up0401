package com.strhzy.api;

import com.strhzy.api.service.CustomerService;
import com.strhzy.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    @Autowired
    @Lazy
    private CustomerService customerService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, customerService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()

                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("MANAGER", "USER")

                        .requestMatchers(HttpMethod.POST, "/api/orders/**", "/api/order-items/**", "/api/reviews/**").hasAnyRole("MANAGER", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/**", "/api/order-items/**", "/api/reviews/**").hasAnyRole("MANAGER", "USER")

                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("MANAGER", "USER")

                        .requestMatchers("/api/**").hasRole("MANAGER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable());

        return http.build();
    }
}
