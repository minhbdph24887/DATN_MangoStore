package com.datn.datn_mangostore.config;

import com.datn.datn_mangostore.service.LoginService;
import com.datn.datn_mangostore.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final LoginService loginService;
    private final UserServiceImpl userService;

    public SecurityConfig(@Lazy LoginService loginService,
                          UserServiceImpl userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/oauth2/authorization/google").authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        http.formLogin(login -> login
                .loginPage("/mangostore/login/from")
                .loginProcessingUrl("login/Form")
                .defaultSuccessUrl("/mangostore/login/success", false));

        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/oauth2/authorization/google")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        loginService.checkLoginGoogleAccount(response, authentication);
                    }
                }));

        http.rememberMe(rem -> rem
                .userDetailsService(userService)
                .rememberMeParameter("remember"));

        return http.build();
    }
}
