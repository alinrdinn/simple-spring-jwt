package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    

    // @PostMapping("/register")
    // public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
    //     List<UserRoleRegion> roles = new ArrayList<>();
    //     roles.add(new U)
    //     roles.add(this.userService.getRole("WARGA"));
    //     User newUser = new User(user.username, user.password, roles);
    //     return ResponseEntity.ok().body(this.userService.createUser(newUser));
    // }

    @GetMapping("/premium-ketuart")
    public ResponseEntity<String>  premiumKetuaRT() {
        return ResponseEntity.ok().body("Premium Ketua RT Endpoint");
    }
    
    @GetMapping("/premium-warga")
    public ResponseEntity<String>  premiumWarga() {
        return ResponseEntity.ok().body("Premium Warga Endpoint");
    }

    @GetMapping("/premium-ketuart-warga")
    public ResponseEntity<String>  premiumKetuaWarga() {
        return ResponseEntity.ok().body("Premium Ketua RT dan Warga Endpoint");
    }

    @GetMapping("/free-ketuart")
    public ResponseEntity<String>  freeKetuaRT() {
        return ResponseEntity.ok().body("Free Ketua RT Endpoint");
    }
    
    @GetMapping("/free-warga")
    public ResponseEntity<String>  freeWarga() {
        return ResponseEntity.ok().body("Free Warga Endpoint");
    }

    @GetMapping("/free-ketuart-warga")
    public ResponseEntity<String>  freeKetuaWarga() {
        return ResponseEntity.ok().body("Free Ketua RT dan Warga Endpoint");
    }
}
