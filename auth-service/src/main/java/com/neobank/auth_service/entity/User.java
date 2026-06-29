package com.neobank.auth_service.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;

@Entity
@Table(name = "users")
public class User {

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean emailVerified;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (emailVerified == null) {
            emailVerified = false;
        }

    }
}