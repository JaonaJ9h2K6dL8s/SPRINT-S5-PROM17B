package project.controllerBe;

import jframework.annotation.Controller;
import jframework.annotation.Url;

/**
 * Contrôleur Voiture pour le Sprint 2-bis
 * Démontre l'auto-découverte des contrôleurs dans différents packages
 */
@Controller
public class Voiture {
    
    private String marque = "Toyota";
    private String modele = "Camry";
    private int annee = 2023;
    private String couleur = "Bleu";
    
    /**
     * Méthode mappée à l'URL /voiture
     */
    @Url("/voiture")
    public String afficherVoiture() {
        System.out.println("Méthode afficherVoiture() appelée");
        return "Voiture : " + marque + " " + modele + " (" + annee + ") - " + couleur;
    }

    /**
     * Méthode mappée à l'URL /voiture/details
     */
    @Url("/voiture/details")
    public String afficherDetailsVoiture() {
        System.out.println("Méthode afficherDetailsVoiture() appelée");
        return "Détails complets : " + marque + " " + modele + ", année " + annee + ", couleur " + couleur + " - Contrôleur auto-découvert";
    }

    /**
     * Méthode mappée à l'URL /voiture/specs
     */
    @Url("/voiture/specs")
    public String afficherSpecifications() {
        System.out.println("Méthode afficherSpecifications() appelée");
        return "Spécifications : Moteur 2.5L, Transmission automatique, 4 portes";
    }

    /**
     * Constructeur par défaut
     */
    public Voiture() {
        System.out.println("Instance de Voiture créée avec @Controller");
    }
}