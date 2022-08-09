package com.example.demo.repositories;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entities.Role;

@DataJpaTest
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName() {
        String roleName = "ADMIN";
        Role role = new Role(roleName);
        roleRepository.save(role);
        Role role2 = roleRepository.findByName(roleName);

        assertThat(role2.getName()).isEqualTo(roleName);
    }
    
}
