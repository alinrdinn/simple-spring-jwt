package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.services.UserServiceImpl;

import java.util.ArrayList;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	// @Bean
	// CommandLineRunner runner(UserServiceImpl userService) {
	// 	return args -> {
	// 		userService.createRole(new Role("ADMIN"));
	// 		userService.createUser(new User("ali", "1234567890", new ArrayList<Role>()));
	// 		userService.addRoleToUser("ali", "ADMIN");
	// 	};
	// }

}
