package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer>{
    Subscription findByName(String name);
}
