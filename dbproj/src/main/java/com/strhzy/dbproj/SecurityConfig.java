package com.strhzy.dbproj;

import com.strhzy.dbproj.services.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UserService userService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("username", "role")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/login", "/auth/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/books",
                                "/authors",
                                "/genres",
                                "/countries",
                                "/years")
                            .hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/add", "/books/edit/**")
                            .hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/users", "/admin/edit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/edit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/authors/add", "/authors/edit/**",
                                "/genres/add", "/genres/edit/**",
                                "/countries/add", "/countries/edit/**",
                                "/years/add", "/years/edit/**")
                            .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/**")
                            .hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/authors/**",
                                "/genres/**",
                                "/countries/**",
                                "/years/**")
                            .hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String username = authentication.getName();
            String role = authentication.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");
            if (role.startsWith("ROLE_")) role = role.substring(5);

            Cookie cookie = new Cookie("username", username);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setHttpOnly(false);
            response.addCookie(cookie);

            Cookie roleCookie = new Cookie("role", role);
            roleCookie.setPath("/");
            roleCookie.setMaxAge(7 * 24 * 60 * 60);
            roleCookie.setHttpOnly(false);
            response.addCookie(roleCookie);

            response.sendRedirect("/");
        };
    }
}
