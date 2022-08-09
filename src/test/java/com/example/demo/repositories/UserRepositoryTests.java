package com.example.demo.repositories;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entities.User;
import com.example.demo.entities.Role;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByName() {
        String username = "alinurdin";
        String password = "12345678";

        User user = new User(username, password, null);

        userRepository.save(user);

        User user2 = userRepository.findByUsername(username);

        assertThat(user2.getUsername()).isEqualTo(username);
    }
    
}
