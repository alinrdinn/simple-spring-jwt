package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.AccessRequirements;
import com.example.demo.entities.Endpoint;
import com.example.demo.entities.Region;
import com.example.demo.entities.Role;
import com.example.demo.entities.Subscription;
import com.example.demo.entities.User;
import com.example.demo.services.EndpointServiceImpl;
import com.example.demo.services.RegionServiceImpl;
import com.example.demo.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableWebSecurity
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner runner(UserServiceImpl userService, RegionServiceImpl regionService, EndpointServiceImpl endpointService) {
		return args -> {
			Map<String, List<String>> rolesMap = Map.of(
				"/premium-ketuart", List.of("KETUA_RT"),
				"/premium-warga", List.of("WARGA"),
				"/premium-ketuart-warga", List.of("WARGA", "KETUA_RT"),
				"/free-ketuart", List.of("KETUA_RT"),
				"/free-warga", List.of("WARGA"),
				"/free-ketuart-warga", List.of("WARGA", "KETUA_RT")
			);

			Map<String, List<String>> subscriptionsMap = Map.of(
				"/premium-ketuart", List.of("PREMIUM"),
				"/premium-warga", List.of("PREMIUM"),
				"/premium-ketuart-warga", List.of("PREMIUM"),
				"/free-ketuart", List.of("FREE", "PREMIUM"),
				"/free-warga", List.of("FREE", "PREMIUM"),
				"/free-ketuart-warga", List.of("FREE", "PREMIUM")
			);

			for (String url : rolesMap.keySet()) {
				AccessRequirements accessRequirements = endpointService.createAccessRequirements(rolesMap.get(url), subscriptionsMap.get(url));
				Endpoint endpoint = new Endpoint();
				endpoint.setUrl(url);
				endpoint.setAccessRequirements(accessRequirements);
				endpointService.createEndpoint(endpoint);
			}

			userService.createRole(new Role("KETUA_RT"));
			userService.createRole(new Role("WARGA"));

			regionService.createSubscription(new Subscription("FREE"));
			regionService.createSubscription(new Subscription("PREMIUM"));
			
			regionService.createRegion(new Region("w1"));
			regionService.createRegion(new Region("w2"));
			regionService.addSubscriptionToRegion("w1", "FREE");
			regionService.addSubscriptionToRegion("w2", "PREMIUM");

			userService.createUser(new User("a", "1234567890", new ArrayList<>()));
			userService.createUser(new User("n", "1234567890", new ArrayList<>()));
			userService.createUser(new User("an", "1234567890", new ArrayList<>()));
			userService.createUser(new User("ann", "1234567890", new ArrayList<>()));
			userService.createUser(new User("anna", "1234567890", new ArrayList<>()));
			userService.addRegionAndRoleToUser("w1", "KETUA_RT", "a");
			userService.addRegionAndRoleToUser("w2", "KETUA_RT", "n");
			userService.addRegionAndRoleToUser("w1", "WARGA", "an");
			userService.addRegionAndRoleToUser("w2", "WARGA", "ann");
			userService.addRegionAndRoleToUser("w1", "WARGA", "anna");
			userService.addRegionAndRoleToUser("w2", "WARGA", "anna");
		};
	}

}
