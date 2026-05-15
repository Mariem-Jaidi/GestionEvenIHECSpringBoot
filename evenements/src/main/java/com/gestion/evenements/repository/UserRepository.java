package com.gestion.evenements.repository;

import com.gestion.evenements.models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data génère automatiquement la requête SELECT par username
public interface UserRepository extends JpaRepository<UserApp, Long> {
    UserApp findByUsername(String username);
}