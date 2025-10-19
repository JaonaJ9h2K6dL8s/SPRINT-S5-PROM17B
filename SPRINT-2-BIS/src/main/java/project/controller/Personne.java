package project.controller;

import jframework.annotation.Controller;
import jframework.annotation.Url;

/**
 * Contrôleur de démonstration pour le Sprint 2-bis
 * Utilise l'annotation @Controller pour l'auto-découverte
 * et @Url pour mapper les méthodes aux URLs
 */
@Controller
public class Personne {
    
    private String nom = "Jean Dupont";
    private int age = 30;
    private String profession = "Développeur";
    
    /**
     * Méthode mappée à l'URL /oui
     * Affiche les informations d'une personne
     */
    @Url("/oui")
    public String afficherInfos() {
        System.out.println("Méthode afficherInfos() appelée pour l'URL /oui");
        return "Informations de la personne : " + nom + ", " + age + " ans";
    }

    /**
     * Méthode mappée à l'URL /personne
     * Affiche les détails d'une personne
     */
    @Url("/personne")
    public String afficherPersonne() {
        System.out.println("Méthode afficherPersonne() appelée pour l'URL /personne");
        return "Personne : " + nom + " (" + profession + ")";
    }

    /**
     * Méthode mappée à l'URL /info
     * Affiche des informations générales
     */
    @Url("/info")
    public String afficherInformationsGenerales() {
        System.out.println("Méthode afficherInformationsGenerales() appelée pour l'URL /info");
        return "Framework Sprint 2-bis - Auto-découverte des contrôleurs activée";
    }

    /**
     * Nouvelle méthode pour le sprint 2-bis
     * Méthode mappée à l'URL /details
     */
    @Url("/details")
    public String afficherDetails() {
        System.out.println("Méthode afficherDetails() appelée pour l'URL /details");
        return "Détails complets : " + nom + ", " + age + " ans, " + profession + " - Sprint 2-bis";
    }

    /**
     * Constructeur par défaut
     */
    public Personne() {
        System.out.println("Instance de Personne créée avec @Controller");
    }
}