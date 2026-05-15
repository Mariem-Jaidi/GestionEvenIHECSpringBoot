package com.gestion.evenements.controllers;

import com.gestion.evenements.models.*;
import com.gestion.evenements.services.gestionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class EvenementController {

    @Autowired
    private gestionnaire gestionnaireEvenements;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("evenements", gestionnaireEvenements.getListeEvenements());
        return "dashboard";
    }

    @GetMapping("/ajouter")
    public String afficherFormulaireAjouter() {
        return "ajouter";
    }

    @PostMapping("/ajouter")
    public String ajouterEvenement(@RequestParam String type,
                                   @RequestParam String nomEvenement,
                                   @RequestParam String heureDebut,
                                   @RequestParam String heureFin,
                                   @RequestParam String lieu,
                                   @RequestParam String description,
                                   @RequestParam int capaciteMax,
                                   @RequestParam int nbreInscrits,
                                   @RequestParam String organisateur,
                                   @RequestParam String statut,
                                   @RequestParam float prix,
                                   @RequestParam String date,
                                   @RequestParam(required = false) String intervenant,
                                   @RequestParam(required = false) String domaine,
                                   @RequestParam(required = false) String materielNecessaire,
                                   @RequestParam(required = false) String niveau,
                                   @RequestParam(required = false) String theme,
                                   @RequestParam(required = false) String refreshments,
                                   RedirectAttributes redirectAttributes) {

        // Validation de la date
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateEvenement = LocalDate.parse(date, formatter);
            if (dateEvenement.isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "La date ne peut pas être dans le passé.");
                return "redirect:/ajouter";
            }
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Format de date invalide.");
            return "redirect:/ajouter";
        }

        // Le lieu doit contenir "IHEC"
        if (!lieu.toUpperCase().contains("IHEC")) {
            redirectAttributes.addFlashAttribute("error", "Le lieu doit être à l'IHEC Carthage (ex: IHEC - Salle A1).");
            return "redirect:/ajouter";
        }

        // L'heure de début doit être entre 09:00 et 18:00
        if (heureDebut.compareTo("09:00") < 0 || heureDebut.compareTo("18:00") > 0) {
            redirectAttributes.addFlashAttribute("error", "L'heure de début doit être entre 09:00 et 18:00.");
            return "redirect:/ajouter";
        }

        // L'heure de fin doit être entre 09:00 et 18:00
        if (heureFin.compareTo("09:00") < 0 || heureFin.compareTo("18:00") > 0) {
            redirectAttributes.addFlashAttribute("error", "L'heure de fin doit être entre 09:00 et 18:00.");
            return "redirect:/ajouter";
        }

        // L'heure de fin doit être après l'heure de début
        if (heureFin.compareTo(heureDebut) <= 0) {
            redirectAttributes.addFlashAttribute("error", "L'heure de fin doit être après l'heure de début.");
            return "redirect:/ajouter";
        }

        Evenement evt = null;

        // PAR CECI — pas de validation obligatoire, valeur vide si non rempli
        if (type.equals("Conference")) {
            evt = new Conference(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, type, organisateur, statut, prix, date,
                    intervenant != null ? intervenant : "",
                    domaine != null ? domaine : "");
        } else if (type.equals("Atelier")) {
            evt = new Atelier(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, type, organisateur, statut, prix, date,
                    materielNecessaire != null ? materielNecessaire : "",
                    niveau != null ? niveau : "débutant");
        } else if (type.equals("EvenementSocial")) {
            evt = new EvenementSocial(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, type, organisateur, statut, prix, date,
                    theme != null ? theme : "",
                    refreshments != null ? refreshments : "");

        } else if (type.equals("Atelier")) {
            if (materielNecessaire == null || materielNecessaire.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le matériel nécessaire est obligatoire pour un atelier.");
                return "redirect:/ajouter";
            }
            evt = new Atelier(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, type, organisateur, statut, prix, date,
                    materielNecessaire, niveau);
        } else if (type.equals("EvenementSocial")) {
            if (theme == null || theme.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le thème est obligatoire pour un événement social.");
                return "redirect:/ajouter";
            }
            evt = new EvenementSocial(nomEvenement, heureDebut, heureFin, lieu, description,
                    capaciteMax, nbreInscrits, type, organisateur, statut, prix, date,
                    theme, refreshments);
        }

        if (evt != null && gestionnaireEvenements.ajouterEvenement(evt)) {
            redirectAttributes.addFlashAttribute("success", "Événement ajouté avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("error", "Impossible d'ajouter l'événement. Vérifiez les informations.");
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModifier(@PathVariable Long id, Model model) {
        Evenement evt = gestionnaireEvenements.getListeEvenements()
                .stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (evt == null) return "redirect:/dashboard";
        model.addAttribute("evenement", evt);
        return "modifier";
    }

    @PostMapping("/modifier/{id}")
    public String modifierEvenement(@PathVariable Long id,
                                    @RequestParam String nomEvenement,
                                    @RequestParam String heureDebut,
                                    @RequestParam String heureFin,
                                    @RequestParam String lieu,
                                    @RequestParam String description,
                                    @RequestParam int capaciteMax,
                                    @RequestParam int nbreInscrits,
                                    @RequestParam String organisateur,
                                    @RequestParam String statut,
                                    @RequestParam float prix,
                                    @RequestParam String date,
                                    RedirectAttributes redirectAttributes) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateEvenement = LocalDate.parse(date, formatter);
            if (dateEvenement.isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "La date ne peut pas être dans le passé.");
                return "redirect:/modifier/" + id;
            }
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Format de date invalide.");
            return "redirect:/modifier/" + id;
        }
        // Le lieu doit contenir "IHEC"
        if (!lieu.toUpperCase().contains("IHEC")) {
            redirectAttributes.addFlashAttribute("error", "Le lieu doit être à l'IHEC Carthage (ex: IHEC - Salle A1).");
            return "redirect:/modifier/" + id;
        }

// Validation des heures
        if (heureDebut.compareTo("09:00") < 0 || heureDebut.compareTo("18:00") > 0) {
            redirectAttributes.addFlashAttribute("error", "L'heure de début doit être entre 09:00 et 18:00.");
            return "redirect:/modifier/" + id;
        }
        if (heureFin.compareTo("09:00") < 0 || heureFin.compareTo("18:00") > 0) {
            redirectAttributes.addFlashAttribute("error", "L'heure de fin doit être entre 09:00 et 18:00.");
            return "redirect:/modifier/" + id;
        }
        if (heureFin.compareTo(heureDebut) <= 0) {
            redirectAttributes.addFlashAttribute("error", "L'heure de fin doit être après l'heure de début.");
            return "redirect:/modifier/" + id;
        }
        Evenement ancien = gestionnaireEvenements.getListeEvenements()
                .stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (ancien == null) {
            redirectAttributes.addFlashAttribute("error", "Événement introuvable.");
            return "redirect:/dashboard";
        }
        ancien.setNomEvenement(nomEvenement);
        ancien.setHeureDebut(heureDebut);
        ancien.setHeureFin(heureFin);
        ancien.setLieu(lieu);
        ancien.setDescription(description);
        ancien.setCapaciteMax(capaciteMax);
        ancien.setNbreInscrits(nbreInscrits);
        ancien.setOrganisateur(organisateur);
        ancien.setStatut(statut);
        ancien.setPrix(prix);
        ancien.setDateEvenement(date);
        gestionnaireEvenements.sauvegarder(ancien);
        redirectAttributes.addFlashAttribute("success", "Événement modifié avec succès !");
        return "redirect:/dashboard";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimerEvenement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gestionnaireEvenements.supprimerParId(id);
        redirectAttributes.addFlashAttribute("success", "Événement supprimé.");
        return "redirect:/dashboard";
    }

    @GetMapping("/rechercher")
    public String rechercher(@RequestParam(required = false) String nom,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String statut,
                             @RequestParam(required = false) String tri,
                             Model model) {
        List<Evenement> resultats = gestionnaireEvenements.getListeEvenements();

        if (nom != null && !nom.isEmpty())
            resultats = resultats.stream()
                    .filter(e -> e.getNomEvenement().toLowerCase().contains(nom.toLowerCase()))
                    .collect(Collectors.toList());

        if (type != null && !type.isEmpty())
            resultats = resultats.stream()
                    .filter(e -> e.getTypeEvenement().equalsIgnoreCase(type))
                    .collect(Collectors.toList());

        if (statut != null && !statut.isEmpty())
            resultats = resultats.stream()
                    .filter(e -> e.getStatut().equalsIgnoreCase(statut))
                    .collect(Collectors.toList());

        if (tri != null) {
            switch (tri) {
                case "nom" -> resultats.sort((a, b) -> a.getNomEvenement().compareToIgnoreCase(b.getNomEvenement()));
                case "date" -> resultats.sort((a, b) -> a.getDateEvenement().compareTo(b.getDateEvenement()));
                case "prix" -> resultats.sort((a, b) -> Float.compare(a.getPrix(), b.getPrix()));
                case "capacite" -> resultats.sort((a, b) -> Integer.compare(b.getCapaciteMax(), a.getCapaciteMax()));
            }
        }

        model.addAttribute("resultats", resultats);
        model.addAttribute("nom", nom);
        model.addAttribute("type", type);
        model.addAttribute("statut", statut);
        model.addAttribute("tri", tri);
        return "rechercher";
    }
}