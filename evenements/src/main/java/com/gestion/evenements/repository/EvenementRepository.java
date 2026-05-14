package com.gestion.evenements.repository;

import com.gestion.evenements.models.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
// JpaRepository fournit automatiquement findAll, save, deleteById, findById, etc.
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    Evenement findByNomEvenement(String nomEvenement);
}