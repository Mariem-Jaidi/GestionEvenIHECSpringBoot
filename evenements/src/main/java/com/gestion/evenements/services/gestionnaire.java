package com.gestion.evenements.services;

import com.gestion.evenements.models.Evenement;
import com.gestion.evenements.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// @Service déclare cette classe comme composant Spring géré par le conteneur IoC
@Service
public class gestionnaire {

    // @Autowired injecte automatiquement le repository Spring Data JPA
    @Autowired
    private EvenementRepository evenementRepository;

    public boolean ajouterEvenement(Evenement e) {
        if (e == null) {
            System.out.println("Evenement invalide, impossible de l'ajouter.");
            return false;
        }
        if (!e.validerEvenement()) {
            System.out.println("Evenement invalide, impossible de l'ajouter.");
            return false;
        }

        if (evenementRepository.findByNomEvenement(e.getNomEvenement()) != null) {
            System.out.println("Evenement avec le meme nom existe deja, impossible de l'ajouter.");
            return false;
        }

        if (e.getStatut().equals("complet") && e.getNbreInscrits() < e.getCapaciteMax()) {
            System.out.println("Statut 'complet' invalide avec le nombre d'inscrits actuel, impossible de l'ajouter.");
            return false;
        }
        if (e.getStatut().equals("ouvert") && e.getNbreInscrits() >= e.getCapaciteMax()) {
            System.out.println("Statut 'ouvert' invalide avec le nombre d'inscrits actuel, impossible de l'ajouter.");
            return false;
        }
        if (e.getStatut().equals("annulé") && e.getNbreInscrits() > 0) {
            System.out.println("Statut 'annulé' invalide avec le nombre d'inscrits actuel, impossible de l'ajouter.");
            return false;
        }
        if (e.getStatut().equals("terminé") && e.getNbreInscrits() == 0) {
            System.out.println("Statut 'terminé' invalide avec le nombre d'inscrits actuel, impossible de l'ajouter.");
            return false;
        }
        if (e.getNbreInscrits() > e.getCapaciteMax()) {
            System.out.println("Nombre d'inscrits dépasse la capacité maximale, impossible de l'ajouter.");
            return false;
        }
        if (e.getCapaciteMax() <= 0) {
            System.out.println("Capacité maximale doit être un nombre positif, impossible de l'ajouter.");
            return false;
        }
        if (e.getHeureFin().compareTo(e.getHeureDebut()) <= 0) {
            System.out.println("Heure de fin doit être après l'heure de début, impossible de l'ajouter.");
            return false;
        }
        if (e.getPrix() < 0) {
            System.out.println("Prix invalide, impossible de l'ajouter.");
            return false;
        }
        if (e.getPrix() > 0 && !e.necessiteInscription()) {
            System.out.println("Événement payant doit nécessiter une inscription, impossible de l'ajouter.");
            return false;
        }
        if (e.getPrix() == 0 && e.necessiteInscription()) {
            System.out.println("Événement gratuit ne doit pas nécessiter une inscription, impossible de l'ajouter.");
            return false;
        }
        if (e.getTypeEvenement().equals("Conférence") && e.getCapaciteMax() < 50) {
            System.out.println("Les conférences doivent avoir une capacité minimale de 50, impossible de l'ajouter.");
            return false;
        }
        if (e.getTypeEvenement().equals("Atelier") && e.getCapaciteMax() < 20) {
            System.out.println("Les ateliers doivent avoir une capacité minimale de 20, impossible de l'ajouter.");
            return false;
        }
        if (e.getTypeEvenement().equals("Evenement social") && e.getCapaciteMax() < 30) {
            System.out.println("Les Evénements sociaux doivent avoir une capacité minimale de 30, impossible de l'ajouter.");
            return false;
        }

        evenementRepository.save(e);
        System.out.println("Événement " + e.getNomEvenement() + " ajouté avec succès.");
        return true;
    }

    public void supprimerEvenement(String nomEvenement) {
        Evenement e = evenementRepository.findByNomEvenement(nomEvenement);
        if (e != null) {
            evenementRepository.delete(e);
            System.out.println("Evenement avec le nom = " + nomEvenement + " supprimé avec succès.");
        } else {
            System.out.println("Aucun evenement trouvé avec le nom = " + nomEvenement);
        }
    }

    public boolean modifierEvenement(String nomEvenement, Evenement nouvelEvenement) {
        if (nouvelEvenement == null || !nouvelEvenement.validerEvenement()) {
            System.out.println("Nouvel evenement invalide, impossible de modifier.");
            return false;
        }

        Evenement ancienEvenement = evenementRepository.findByNomEvenement(nomEvenement);
        if (ancienEvenement == null) {
            System.out.println("Aucun evenement trouvé avec le nom = " + nomEvenement);
            return false;
        }

        ancienEvenement.setNomEvenement(nouvelEvenement.getNomEvenement());
        ancienEvenement.setLieu(nouvelEvenement.getLieu());
        ancienEvenement.setDateEvenement(nouvelEvenement.getDateEvenement());
        ancienEvenement.setHeureDebut(nouvelEvenement.getHeureDebut());
        ancienEvenement.setHeureFin(nouvelEvenement.getHeureFin());
        ancienEvenement.setDescription(nouvelEvenement.getDescription());
        ancienEvenement.setTypeEvenement(nouvelEvenement.getTypeEvenement());
        ancienEvenement.setOrganisateur(nouvelEvenement.getOrganisateur());
        ancienEvenement.setPrix(nouvelEvenement.getPrix());
        ancienEvenement.setCapaciteMax(nouvelEvenement.getCapaciteMax());
        ancienEvenement.setNbreInscrits(nouvelEvenement.getNbreInscrits());
        ancienEvenement.setStatut(nouvelEvenement.getStatut());

        evenementRepository.save(ancienEvenement);
        System.out.println("Evenement avec le nom = " + nomEvenement + " modifié avec succès.");
        return true;
    }

    public Evenement consulterEvenement(String nomEvenement) {
        Evenement e = evenementRepository.findByNomEvenement(nomEvenement);
        if (e != null) {
            System.out.println("Evenement trouvé : ");
            e.afficherDetails();
            return e;
        }
        System.out.println("Aucun evenement trouvé avec le nom = " + nomEvenement);
        return null;
    }

    public void afficherTousEvenements() {
        List<Evenement> liste = evenementRepository.findAll();
        if (liste.isEmpty()) {
            System.out.println("Aucun evenement disponible.");
            return;
        }
        System.out.println("\n========================================");
        System.out.println("📋 LISTE DE TOUS LES ÉVÉNEMENTS (" + liste.size() + ")");
        System.out.println("========================================\n");
        for (int i = 0; i < liste.size(); i++) {
            System.out.println("Événement " + (i + 1) + " :");
            liste.get(i).afficherDetails();
            System.out.println("----------------------------------------\n");
        }
    }

    public List<Evenement> getListeEvenements() {
        return evenementRepository.findAll();
    }
}