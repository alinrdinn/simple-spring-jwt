package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "endpoints")
public class Endpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String url;

    @OneToOne(cascade = CascadeType.MERGE)
    private AccessRequirements accessRequirements;

    public Endpoint() {
    }
    
    public Endpoint(String url, AccessRequirements accessRequirements) {
        this.url = url;
        this.accessRequirements = accessRequirements;
    }

    public Integer getId() {
        return id;
    }
    
    public String getUrl() {
        return url;
    }
    
    public AccessRequirements getAccessRequirements() {
        return accessRequirements;
    }
    public void setId(Integer  id) {
        this.id = id;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setAccessRequirements(AccessRequirements accessRequirements) {
        this.accessRequirements = accessRequirements;
    }
}