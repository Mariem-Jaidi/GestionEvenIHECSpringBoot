package com.gestion.evenements.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("EvenementSocial")
public class EvenementSocial extends Evenement {
    private String theme;
    private String refreshments;

    public EvenementSocial(String nomEvenement, String heureDebut, String heureFin, String lieu,
                           String description, int capaciteMax, int nbreInscrits, String typeEvenement, String organisateur,
                           String statut, float prix, String date, String theme, String refreshments) {
        super(nomEvenement, heureDebut, heureFin, lieu, description, capaciteMax, nbreInscrits,
                typeEvenement, organisateur, statut, prix, date);
        this.theme = theme;
        this.refreshments = refreshments;
    }

    public EvenementSocial() { super(); }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getRefreshments() { return refreshments; }
    public void setRefreshments(String refreshments) { this.refreshments = refreshments; }

    @Override
    public void afficherDetails() {
        System.out.println("=== Événement Social ===");
        System.out.println("Nom: " + nomEvenement);
        System.out.println("Thème: " + theme);
        System.out.println("Date: " + date);
        System.out.println("Heure: " + heureDebut + " - " + heureFin);
        System.out.println("Lieu: " + lieu);
        System.out.println("Description: " + description);
        System.out.println("Capacité: " + nbreInscrits + "/" + capaciteMax);
        System.out.println("Rafraîchissements: " + refreshments);
        System.out.println("Prix: " + prix + " DT");
        System.out.println("Organisateur: " + organisateur);
        System.out.println("Statut: " + statut);
    }

    @Override
    public boolean necessiteInscription() {
        return true;
    }

    @Override
    public String toString() {
        return "EvenementSocial [theme=" + theme + ", refreshments=" + refreshments + ", nomEvenement="
                + nomEvenement + ", heureDebut=" + heureDebut + ", heureFin=" + heureFin + ", lieu=" + lieu
                + ", description=" + description + ", capaciteMax=" + capaciteMax + ", nbreInscrits="
                + nbreInscrits + ", typeEvenement=" + typeEvenement + ", organisateur=" + organisateur
                + ", statut=" + statut + ", prix=" + prix + ", date=" + date + "]";
    }
}