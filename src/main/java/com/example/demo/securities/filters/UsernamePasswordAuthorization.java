package com.example.demo.securities.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsernamePasswordAuthorization extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                
                Map<String, Object> roles = decodedJWT.getClaim("roles").asMap();
                Map<String, Object> subscriptions = decodedJWT.getClaim("subscriptions").asMap();
                List<String> rolesRegion = (List<String>) roles.get(request.getHeader("RegionName"));
                List<String> subscriptionRegion = (List<String>) subscriptions.get(request.getHeader("RegionName"));

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (String role : rolesRegion) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
                for (String subscription : subscriptionRegion) {
                    authorities.add(new SimpleGrantedAuthority("SUB_" + subscription));
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setHeader("Error", e.getMessage());
                response.setStatus(403);
                Map<String, String> errors = new HashMap<>();
                errors.put("errorMessage", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
