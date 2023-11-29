package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Region;

public interface RegionRepository extends JpaRepository<Region, Integer>{
    Region findByName(String name);
}
