package com.example.demo.securities;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import com.example.demo.repositories.EndpointRepository;
import com.example.demo.securities.filters.UsernamePasswordAuthentication;
import com.example.demo.securities.filters.UsernamePasswordAuthorization;
import com.example.demo.services.EndpointServiceImpl;

@Configuration
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EndpointServiceImpl endpointServiceImpl;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder bCryptPasswordEncoder, EndpointServiceImpl endpointServiceImpl) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = (BCryptPasswordEncoder) bCryptPasswordEncoder;
        this.endpointServiceImpl = endpointServiceImpl;
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
            .authorizeRequests().accessDecisionManager(accessDecisionManager())
            .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
            .and().authorizeRequests().anyRequest().authenticated()
            .and().addFilter(usernamePasswordAuth)
            .addFilterBefore(new UsernamePasswordAuthorization(), UsernamePasswordAuthentication.class)
            .build();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(
            new WebExpressionVoter(),
            new RoleSubscriptionVoter(endpointServiceImpl)
        );
        return new UnanimousBased(decisionVoters);
    }
}
