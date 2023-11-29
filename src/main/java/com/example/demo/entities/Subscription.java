package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.Id;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String name;
    
    public Subscription(){}
    public Subscription(String name) {
        this.name = name;
    }

    // getters and setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}