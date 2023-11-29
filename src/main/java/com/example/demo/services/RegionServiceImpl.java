package com.example.demo.services;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Region;
import com.example.demo.entities.Subscription;
import com.example.demo.repositories.RegionRepository;
import com.example.demo.repositories.SubscriptionRepository;

@Service @Transactional
public class RegionServiceImpl implements IRegionService {

    private final RegionRepository regionRepository;
    private final SubscriptionRepository subscriptionRepository;

    public RegionServiceImpl(RegionRepository regionRepository, SubscriptionRepository subscriptionRepository) {
        this.regionRepository = regionRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Region getRegion(String name) {
        return this.regionRepository.findByName(name);
    }

    public Region createRegion(Region region) {
        return this.regionRepository.save(region);
    }

    public Subscription getSubscription(String name) {
        return this.subscriptionRepository.findByName(name);
    }

    public Subscription createSubscription(Subscription subscription) {
        return this.subscriptionRepository.save(subscription);
    }

    public Region addSubscriptionToRegion(String regionName, String subscriptionName) {
        Region region = this.regionRepository.findByName(regionName);
        Subscription subscription = this.subscriptionRepository.findByName(subscriptionName);
        region.getSubscriptions().add(subscription);
        return region;
    }
}
