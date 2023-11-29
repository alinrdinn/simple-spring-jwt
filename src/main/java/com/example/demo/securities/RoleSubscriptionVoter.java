package com.example.demo.securities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.example.demo.services.EndpointServiceImpl;

@Component
public class RoleSubscriptionVoter implements AccessDecisionVoter<Object> {

    private EndpointServiceImpl endpointServiceImpl;

    public RoleSubscriptionVoter(EndpointServiceImpl endpointServiceImpl) {
        this.endpointServiceImpl = endpointServiceImpl;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        FilterInvocation filterInvocation = (FilterInvocation) object;

        String url = filterInvocation.getRequestUrl();
        List<String> endpointRoles = endpointServiceImpl.getRolesAccessRequirementsFromEndpointUrl(url);
        List<String> endpointSubscriptions = endpointServiceImpl.getSubscriptionsAccessRequirementsFromEndpointUrl(url);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
            .filter(a -> a.getAuthority().startsWith("ROLE_"))
            .map(a -> a.getAuthority().substring("ROLE_".length()))
            .collect(Collectors.toList());

        
        List<String> subscriptions = authorities.stream()
            .filter(a -> a.getAuthority().startsWith("SUB_"))
            .map(a -> a.getAuthority().substring("SUB_".length()))
            .collect(Collectors.toList());

        List<String> intersectionRoles = new ArrayList<>(roles);
        intersectionRoles.retainAll(endpointRoles);
        
        List<String> intersectionSubs = new ArrayList<>(subscriptions);
        intersectionSubs.retainAll(endpointSubscriptions);

        if (!intersectionRoles.isEmpty() && !intersectionSubs.isEmpty()) {
            return ACCESS_GRANTED;
        } else {
            return ACCESS_DENIED;
        }
    }
}
