package com.example.demo.securities.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public UsernamePasswordAuthentication(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = null;
        String password = null;
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        String line = null;
        try {
            BufferedReader bufferedReader = request.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            /* report an error */ }
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(stringBuffer.toString());
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        Map<String, Set<String>> rolesPerRegion = new HashMap<>();
        Map<String, Set<String>> subscriptionsPerRegion = new HashMap<>();

        for (GrantedAuthority s : user.getAuthorities()) {
            String[] parts = s.getAuthority().split("\\.");
            String regionName = parts[0];
            String subscriptionName = parts[1];
            String roleName = parts[2];

            rolesPerRegion.computeIfAbsent(regionName, k -> new HashSet<>()).add(roleName);
            subscriptionsPerRegion.computeIfAbsent(regionName, k -> new HashSet<>()).add(subscriptionName);   
        }

        Map<String, List<String>> convertedRolesPerRegion = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : rolesPerRegion.entrySet()) {
            convertedRolesPerRegion.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        
        Map<String, List<String>> convertedSubscriptionsPerRegion = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : subscriptionsPerRegion.entrySet()) {
            convertedSubscriptionsPerRegion.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", convertedRolesPerRegion)
                .withClaim("subscriptions", convertedSubscriptionsPerRegion)
                .sign(algorithm);
        
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
