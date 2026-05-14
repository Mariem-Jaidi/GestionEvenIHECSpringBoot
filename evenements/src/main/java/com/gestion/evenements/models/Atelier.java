package com.gestion.evenements.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Atelier")
public class Atelier extends Evenement {
    private String materielNecessaire;
    private String niveau; // débutant, intermédiaire, avancé

    public Atelier(String nomEvenement, String heureDebut, String heureFin, String lieu, String description,
                   int capaciteMax, int nbreInscrits, String typeEvenement, String organisateur, String statut, float prix,
                   String date, String materielNecessaire, String niveau) {
        super(nomEvenement, heureDebut, heureFin, lieu, description, capaciteMax, nbreInscrits, "Atelier",
                organisateur, "complet", prix, date);
        this.materielNecessaire = materielNecessaire;
        this.niveau = niveau;
    }

    public Atelier() { super(); }

    public String getMaterielNecessaire() { return materielNecessaire; }
    public void setMaterielNecessaire(String materielNecessaire) { this.materielNecessaire = materielNecessaire; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public void afficherDetails() {
        System.out.println("Atelier de niveau: " + niveau + " nécessitant le matériel suivant: " + materielNecessaire + ".");
    }

    public boolean necessiteInscription() {
        return true; // car un atelier nécessite d'inscription
    }
}