package com.gestion.evenements.models;

import jakarta.persistence.*;

// Entité représentant un utilisateur de l'application avec son rôle
@Entity
@Table(name = "users")
public class UserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // ROLE_ADMIN ou ROLE_VISITEUR
    @Column(nullable = false)
    private String role;

    public UserApp() {}

    public UserApp(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}