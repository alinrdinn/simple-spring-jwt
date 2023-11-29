package com.example.demo.services;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Region;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.entities.UserRoleRegion;
import com.example.demo.repositories.RegionRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRoleRegionRepository;

@Service @Transactional
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRegionRepository userRoleRegionRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRoleRegionRepository userRoleRegionRepository, RegionRepository regionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRegionRepository = userRoleRegionRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public Role getRole(String name) {
        return this.roleRepository.findByName(name);
    }

    @Override
    public Role createRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public UserRoleRegion addRegionAndRoleToUser(String regionName, String roleName, String username) {
        Region region = this.regionRepository.findByName(regionName);
        Role role = this.roleRepository.findByName(roleName);
        User user = this.userRepository.findByUsername(username);
        return this.userRoleRegionRepository.save(new UserRoleRegion(user, role, region));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        List<UserRoleRegion> userRoleRegions = userRoleRegionRepository.findByUser(user);
        List<String> result = userRoleRegions.stream()
                .flatMap(userRoleRegion -> userRoleRegion.getRegion().getSubscriptions().stream()
                        .map(subscription -> userRoleRegion.getRegion().getName() + "." + subscription.getName() + "."
                                + userRoleRegion.getRole().getName()))
                .collect(Collectors.toList());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        result.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    
}
