package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.AccessRequirements;
import com.example.demo.entities.Endpoint;

public interface IEndpointService {

    Endpoint getEndpoint(String url);

    Endpoint createEndpoint(Endpoint endpoint);

    AccessRequirements createAccessRequirements(List<String> roles, List<String> subscriptions);

    Endpoint addAccessRequirementsToEndpoint(String url, AccessRequirements accessRequirements);
}