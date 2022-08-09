package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.UserDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.services.UserServiceImpl;

@RestController
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>>  getAllUsers() {
        return ResponseEntity.ok().body(this.userService.getAllUsers());
    }
    

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        List<Role> roles = new ArrayList<>();
        roles.add(this.userService.getRole("USER"));
        User newUser = new User(user.username, user.password, roles);
        return ResponseEntity.ok().body(this.userService.createUser(newUser));

    }
}
