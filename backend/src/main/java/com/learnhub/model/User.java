package com.learnhub.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public String name;
    @Column(nullable = false, unique = true) public String email;
    @Column(name = "password_hash", nullable = false) public String passwordHash;
    @Column(nullable = false) public String role = "STUDENT";
    @Column(name = "created_at") public Instant createdAt = Instant.now();
}
