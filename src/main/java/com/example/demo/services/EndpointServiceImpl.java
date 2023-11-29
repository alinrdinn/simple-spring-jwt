package com.example.demo.services;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.example.demo.entities.AccessRequirements;
import com.example.demo.entities.Endpoint;
import com.example.demo.repositories.AccessRequirementsRepository;
import com.example.demo.repositories.EndpointRepository;

@Service @Transactional
public class EndpointServiceImpl implements IEndpointService {

    private EndpointRepository endpointRepository;
    private AccessRequirementsRepository accessRequirementsRepository;

    public EndpointServiceImpl(EndpointRepository endpointRepository, AccessRequirementsRepository accessRequirementsRepository) {
        this.endpointRepository = endpointRepository;
        this.accessRequirementsRepository = accessRequirementsRepository;
    }

    @Override
    public Endpoint getEndpoint(String url) {
        return endpointRepository.findByUrl(url);
    }

    @Override
    public Endpoint createEndpoint(Endpoint endpoint) {
        return endpointRepository.save(endpoint);
    }

    public List<String> getRolesAccessRequirementsFromEndpointUrl(String url) {
        Hibernate.initialize(endpointRepository.findByUrl(url).getAccessRequirements().getRoles());
        return endpointRepository.findByUrl(url).getAccessRequirements().getRoles();
    }

    public List<String> getSubscriptionsAccessRequirementsFromEndpointUrl(String url) {
        Hibernate.initialize(endpointRepository.findByUrl(url).getAccessRequirements().getSubscriptions());
        return endpointRepository.findByUrl(url).getAccessRequirements().getSubscriptions();
    }


    @Override
    public AccessRequirements createAccessRequirements(List<String> roles, List<String> subscriptions) {
        AccessRequirements accessRequirements = new AccessRequirements();
        accessRequirements.setRoles(roles);
        accessRequirements.setSubscriptions(subscriptions);
        return accessRequirementsRepository.save(accessRequirements);
    }

    @Override
    public Endpoint addAccessRequirementsToEndpoint(String url, AccessRequirements accessRequirements) {
        Endpoint endpoint = getEndpoint(url);
        if (endpoint != null) {
            endpoint.setAccessRequirements(accessRequirements);
            endpoint = createEndpoint(endpoint);
        }
        return endpoint;
    }
}
