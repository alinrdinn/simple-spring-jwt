package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Endpoint;

public interface EndpointRepository extends JpaRepository<Endpoint, Integer>{
    Endpoint findByUrl(String url);
}
