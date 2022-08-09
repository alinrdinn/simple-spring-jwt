package com.example.demo.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.securities.filters.UsernamePasswordAuthentication;
import com.example.demo.securities.filters.UsernamePasswordAuthorization;

@Configuration
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = (BCryptPasswordEncoder) bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        UsernamePasswordAuthentication usernamePasswordAuth = new UsernamePasswordAuthentication(authenticationManager);
        usernamePasswordAuth.setFilterProcessesUrl("/login");
        return http.cors().and().csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authenticationManager(authenticationManager)
            .authorizeRequests().antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
            .and().authorizeRequests().antMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ADMIN")
            .and().authorizeRequests().anyRequest().authenticated()
            .and().addFilter(usernamePasswordAuth)
            .addFilterBefore(new UsernamePasswordAuthorization(), UsernamePasswordAuthentication.class)
            .build();
    }
}
