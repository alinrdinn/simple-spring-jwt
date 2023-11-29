package com.example.demo.services;

import com.example.demo.entities.Region;
import com.example.demo.entities.Subscription;

public interface IRegionService {

    Region getRegion(String name);

    Region createRegion(Region region);

    Subscription getSubscription(String name);

    Subscription createSubscription(Subscription subscription);

    Region addSubscriptionToRegion(String regionName, String subscriptionName);
}
