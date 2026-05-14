package com.gestion.evenements.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.gestion.evenements.models.*;
import com.gestion.evenements.services.gestionnaire;

// @Controller marks this class as the web layer that handles HTTP requests
@Controller
public class EvenementController {

    // @Autowired injects the gestionnaire bean Spring already created
    @Autowired
    private gestionnaire gestionnaireEvenements;

    // Dashboard
    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    // Page ajouter
    @GetMapping("/ajouter")
    public String afficherFormulaireAjouter() {
        return "ajouter";
    }

    // Traitement du formulaire ajouter
    @PostMapping("/ajouter")
    public String ajouterEvenement(
            @RequestParam String typeEvenement,
            @RequestParam String nomEvenement,
            @RequestParam String date,
            @RequestParam String heureDebut,
            @RequestParam String heureFin,
            @RequestParam String lieu,
            @RequestParam String description,
            @RequestParam int capaciteMax,
            @RequestParam int nbreInscrits,
            @RequestParam String organisateur,
            @RequestParam String statut,
            @RequestParam float prix,
            @RequestParam(required = false) String intervenant,
            @RequestParam(required = false) String domaine,
            @RequestParam(required = false) String materielNecessaire,
            @RequestParam(required = false) String niveau,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String refreshments,
            Model model) {

        Evenement e = null;

        if (typeEvenement.equals("Conférence")) {
            e = new Conference(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, typeEvenement, organisateur, statut, prix, date,
                    intervenant != null ? intervenant : "",
                    domaine != null ? domaine : "");
        } else if (typeEvenement.equals("Atelier")) {
            e = new Atelier(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, typeEvenement, organisateur, statut, prix, date,
                    materielNecessaire != null ? materielNecessaire : "",
                    niveau != null ? niveau : "");
        } else {
            e = new EvenementSocial(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, typeEvenement, organisateur, statut, prix, date,
                    theme != null ? theme : "",
                    refreshments != null ? refreshments : "");
        }

        boolean succes = gestionnaireEvenements.ajouterEvenement(e);
        if (succes) {
            model.addAttribute("message", "Événement '" + nomEvenement + "' ajouté avec succès !");
            model.addAttribute("succes", true);
        } else {
            model.addAttribute("message", "Erreur : impossible d'ajouter l'événement. Vérifiez les données.");
            model.addAttribute("succes", false);
        }
        return "ajouter";
    }

    // Page rechercher
    @GetMapping("/rechercher")
    public String afficherFormulaireRechercher() {
        return "rechercher";
    }

    // Traitement de la recherche
    @PostMapping("/rechercher")
    public String rechercherEvenement(@RequestParam String nomRecherche, Model model) {
        if (nomRecherche == null || nomRecherche.trim().isEmpty()) {
            model.addAttribute("erreur", "Veuillez entrer le nom d'un événement.");
            return "rechercher";
        }
        Evenement evt = gestionnaireEvenements.consulterEvenement(nomRecherche.trim());
        if (evt == null) {
            model.addAttribute("erreur", "Aucun événement trouvé avec le nom : \"" + nomRecherche + "\"");
        } else {
            model.addAttribute("evenement", evt);
        }
        model.addAttribute("nomRecherche", nomRecherche);
        return "rechercher";
    }
}