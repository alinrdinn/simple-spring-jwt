package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;

public interface IUserService {
    List<User> getAllUsers();
    User getUser(String username);
    User createUser(User user);
    Role createRole(Role role);
    Role getRole(String name);
    User addRoleToUser(String username, String name);
}
