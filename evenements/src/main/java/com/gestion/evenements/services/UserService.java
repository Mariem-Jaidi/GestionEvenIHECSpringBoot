package com.gestion.evenements.services;

import com.gestion.evenements.models.UserApp;
import com.gestion.evenements.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

// @Service implémente UserDetailsService pour que Spring Security sache comment charger un user
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crée les comptes par défaut au démarrage si la table est vide
    @PostConstruct
    public void initUsers() {
        if (userRepository.count() == 0) {
            userRepository.save(new UserApp("adminUni", passwordEncoder.encode("ihec2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("hecfa", passwordEncoder.encode("hecfa2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("artrev", passwordEncoder.encode("artrev2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("lions", passwordEncoder.encode("lions2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("enactus", passwordEncoder.encode("enactus2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("aiesec", passwordEncoder.encode("aiesec2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("mmt", passwordEncoder.encode("mmt2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("libertad", passwordEncoder.encode("libertad2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("ihecnews", passwordEncoder.encode("ihecnews2025"), "ROLE_ADMIN"));
            userRepository.save(new UserApp("visiteur", passwordEncoder.encode("visiteur123"), "ROLE_VISITEUR"));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApp user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Utilisateur introuvable: " + username);
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}