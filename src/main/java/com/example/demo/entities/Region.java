package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.util.List;

@Entity
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Subscription> subscriptions;

    @OneToMany(mappedBy = "region")
    List<UserRoleRegion> userRoleRegions;

    public Region() {}
    public Region(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
    public List<UserRoleRegion> getUserRoleRegions() {
        return userRoleRegions;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
    public void setUserRoleRegions(List<UserRoleRegion> userRoleRegions) {
        this.userRoleRegions = userRoleRegions;
    }
}

