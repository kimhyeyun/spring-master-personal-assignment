package com.example.springmasterpersonalassignment.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((request) ->
                request
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.formLogin((login) ->
                login
                        .loginPage("/api/user/login")
                        .loginProcessingUrl("/api/user/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/api/user/login?error")
                        .permitAll()
        );

        return http.build();
    }
}
