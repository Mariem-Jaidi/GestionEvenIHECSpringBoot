package com.gestion.evenements.models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@Table(name = "evenement")
public abstract class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String nomEvenement;
    protected String heureDebut;
    protected String heureFin;
    protected String lieu;
    protected String description;
    protected int capaciteMax;
    protected int nbreInscrits;
    protected String typeEvenement;
    protected String organisateur;
    protected String statut; //ouvert , complet , annulé ou terminé
    protected float prix;
    protected String date;

    public Evenement(String nomEvenement, String heureDebut, String heureFin, String lieu,
                     String description, int capaciteMax, int nbreInscrits, String typeEvenement, String organisateur,
                     String statut, float prix, String date) {
        super();
        this.nomEvenement = nomEvenement;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.lieu = lieu;
        this.description = description;
        this.capaciteMax = capaciteMax;
        this.nbreInscrits = Math.min(nbreInscrits, capaciteMax);
        this.typeEvenement = typeEvenement;
        this.organisateur = organisateur;
        this.statut = statut;
        this.prix = prix;
        this.date = date;
    }

    public Evenement() {
        super();
    }

    public Long getId() { return id; }
    public String getNomEvenement() { return nomEvenement; }
    public void setNomEvenement(String nomEvenement) { this.nomEvenement = nomEvenement; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }
    public int getNbreInscrits() { return nbreInscrits; }
    public void setNbreInscrits(int nbreInscrits) { this.nbreInscrits = Math.min(nbreInscrits, capaciteMax); }
    public String getTypeEvenement() { return typeEvenement; }
    public void setTypeEvenement(String typeEvenement) { this.typeEvenement = typeEvenement; }
    public String getOrganisateur() { return organisateur; }
    public void setOrganisateur(String organisateur) { this.organisateur = organisateur; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public float getPrix() { return prix; }
    public void setPrix(float prix) { this.prix = prix; }
    public String getDateEvenement() { return date; }
    public void setDateEvenement(String dateEvenement) { date = dateEvenement; }

    public abstract void afficherDetails();
    public abstract boolean necessiteInscription();

    public boolean validerEvenement() {
        if (nomEvenement == null || nomEvenement.isEmpty()) return false;
        if (capaciteMax <= 0) return false;
        if (lieu == null || lieu.isEmpty() ||
                heureDebut == null || heureDebut.isEmpty() ||
                heureFin == null || heureFin.isEmpty() ||
                typeEvenement == null || typeEvenement.isEmpty() ||
                organisateur == null || organisateur.isEmpty()) return false;
        if (nbreInscrits < 0) return false;
        return true;
    }

    public boolean estComplet() {
        return nbreInscrits >= capaciteMax;
    }

    public void annulerEvenement() {
        this.statut = "Annulé";
    }

    @Override
    public String toString() {
        return "Evenement [nomEvenement=" + nomEvenement + ", heureDebut=" + heureDebut
                + ", heureFin=" + heureFin + ", lieu=" + lieu + ", description=" + description + ", capaciteMax="
                + capaciteMax + ", nbreInscrits=" + nbreInscrits + ", typeEvenement=" + typeEvenement
                + ", organisateur=" + organisateur + ", statut=" + statut + "]";
    }
}