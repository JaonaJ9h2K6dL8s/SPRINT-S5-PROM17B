package test;

import com.monframework.ModelView;
import com.monframework.Url;

public class Utilisateur {
    
    @Url("inscription")  // ⚠️ BIEN ÉCRIT SANS ESPACES!
    public ModelView inscrire(String nom) {
        System.out.println("🎯 Méthode inscrire appelée avec: " + nom);
        
        ModelView mv = new ModelView("/result.jsp");  // ⚠️ AJOUTE LE / SI BESOIN
        mv.addObject("nom", nom);
        mv.addObject("action", "inscription");
        mv.addObject("message", "Inscription réussie pour: " + nom);
        return mv;
    }
}