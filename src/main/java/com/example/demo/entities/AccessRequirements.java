package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import java.util.List;

@Entity
@Table(name = "access_requirements")
public class AccessRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ElementCollection
    private List<String> roles;

    @ElementCollection
    private List<String> subscriptions;

    public AccessRequirements() {
    }

    public AccessRequirements(List<String> roles, List<String> subscriptions) {
        this.roles = roles;
        this.subscriptions = subscriptions;
    }
    
    public Integer getId() {
        return id;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public List<String> getSubscriptions() {
        return subscriptions;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }    
    
}