package test;

import com.monframework.ModelView;
import com.monframework.RequestParam;
import com.monframework.Url;

public class Utilisateur {
    
    @Url("inscription")  // ⚠️ BIEN ÉCRIT SANS ESPACES!
    public ModelView inscrire(@RequestParam("nom") String nom) {
        System.out.println("🎯 Méthode inscrire appelée avec: " + nom);
        
        ModelView mv = new ModelView("/result.jsp");  // ⚠️ AJOUTE LE / SI BESOIN
        mv.addObject("nom", nom);
        mv.addObject("action", "inscription");
        mv.addObject("message", "Inscription réussie pour: " + nom);
        return mv;
    }
    
    @Url("profil")
    public ModelView voirProfil(@RequestParam("userId") Integer userId) {
        System.out.println("🎯 Méthode voirProfil appelée avec: " + userId);
        
        ModelView mv = new ModelView("/result.jsp");
        mv.addObject("userId", userId);
        mv.addObject("action", "profil");
        return mv;
    }
}