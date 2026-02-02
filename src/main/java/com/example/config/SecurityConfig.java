package com.example.config;

import com.example.security.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean  
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // 🔓 Public APIs
                .requestMatchers(
                    "/user/login",
                    "/user/register",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // 🔓 Public product view
                .requestMatchers(HttpMethod.GET, "/products/**")
                .permitAll()

                // 👑 ADMIN only – add/update/delete product
                .requestMatchers(HttpMethod.POST, "/products/add").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/products/update/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products/delete/**").hasRole("ADMIN")

                // 👤 USER only – cart
                .requestMatchers(HttpMethod.POST, "/cart/add").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/cart/my").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/order/place").hasRole("USER")

                .requestMatchers(HttpMethod.POST, "/order/place").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/order/my").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/order/all").hasRole("ADMIN")

                // 🔒 Everything else needs login
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 PasswordEncoder bean – जरूरी है!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
