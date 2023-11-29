package com.example.demo.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.User;
import com.example.demo.entities.UserRoleRegion;

public interface UserRoleRegionRepository extends JpaRepository<UserRoleRegion, Integer>{
    List<UserRoleRegion> findByUser(User user);
}
