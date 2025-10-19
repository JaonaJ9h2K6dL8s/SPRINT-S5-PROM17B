package project.controllerKely;

import jframework.annotation.Controller;
import jframework.annotation.Url;

/**
 * Contrôleur Materiaux pour le Sprint 2-bis
 * Démontre l'auto-découverte des contrôleurs dans différents packages
 */
@Controller
public class Materiaux {
    
    private String[] materiaux = {"Acier", "Aluminium", "Plastique", "Bois", "Verre"};
    private String fournisseur = "MatériauX Pro";
    
    /**
     * Méthode mappée à l'URL /materiaux
     */
    @Url("/materiaux")
    public String afficherMateriaux() {
        System.out.println("Méthode afficherMateriaux() appelée");
        StringBuilder result = new StringBuilder("Matériaux disponibles : ");
        for (int i = 0; i < materiaux.length; i++) {
            result.append(materiaux[i]);
            if (i < materiaux.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    /**
     * Méthode mappée à l'URL /materiaux/fournisseur
     */
    @Url("/materiaux/fournisseur")
    public String afficherFournisseur() {
        System.out.println("Méthode afficherFournisseur() appelée");
        return "Fournisseur principal : " + fournisseur + " - Contrôleur auto-découvert";
    }

    /**
     * Méthode mappée à l'URL /materiaux/count
     */
    @Url("/materiaux/count")
    public String compterMateriaux() {
        System.out.println("Méthode compterMateriaux() appelée");
        return "Nombre total de matériaux : " + materiaux.length;
    }

    /**
     * Constructeur par défaut
     */
    public Materiaux() {
        System.out.println("Instance de Materiaux créée avec @Controller");
    }
}