package com.example.demo.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

public class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    private AutoCloseable autoCloseable;

    private UserServiceImpl userServiceImpl;


    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        userServiceImpl = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void testAddRoleToUser() {
        String roleName = "ADMIN";
        Role role = new Role(roleName);

        String username = "alinurdin";
        String password = "12345678";
        User user = new User(username, password, new ArrayList<>());

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(roleRepository.findByName(roleName)).thenReturn(role);

        User user2 = userServiceImpl.addRoleToUser(username, roleName);
        assertThat(user2.getRoles()).contains(role);
        
    }

    @Test
    void testCreateRole() {
        String roleName = "ADMIN";
        Role role = new Role(roleName);

        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor
            .forClass(Role.class);

        userServiceImpl.createRole(role);

        verify(roleRepository).save(roleArgumentCaptor.capture());

        Role role2 = roleArgumentCaptor.getValue();

        assertThat(role).isEqualTo(role2);

    }

    @Test
    void testCreateUser() {
        String username = "alinurdin";
        String password = "12345678";
        User user = new User(username, password, null);
        userServiceImpl.createUser(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor
            .forClass(User.class);
        
        verify(userRepository).save(userArgumentCaptor.capture());

        User user2 = userArgumentCaptor.getValue();

        assertThat(user).isEqualTo(user2);

    }

    @Test
    void testGetAllUsers() {
        userServiceImpl.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void testGetUser() {
        String username = "alinurdin";
        userServiceImpl.getUser(username);
        verify(userRepository).findByUsername(username);
    }

}
