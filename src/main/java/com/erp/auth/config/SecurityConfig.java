package com.erp.auth.config;

import com.erp.auth.service.UserInfoDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
//        UserDetails admin = User.withUsername("admin")
//                .password(encoder.encode("password"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withUsername("user")
//                .password(encoder.encode("password"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
        return new UserInfoDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/v1/health", "/auth/v1/new").permitAll()
                .and().authorizeHttpRequests().requestMatchers("/auth/v1/**").authenticated()
//                .and().httpBasic()
                .and().formLogin()
                .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
