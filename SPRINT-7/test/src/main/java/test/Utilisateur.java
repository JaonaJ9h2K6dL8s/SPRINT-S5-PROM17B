package test;

import com.monframework.ModelView;
import com.monframework.Url;

public class Utilisateur {
    
    @Url(value = "getgetrequet", method = "GET")
    public ModelView getGetRequet(String paramGet, String nomGet, String emailGet) {
        System.out.println("🎯 Méthode GET - getGetRequet appelée avec: " + paramGet + ", " + nomGet + ", " + emailGet);
        
        ModelView mv = new ModelView("/formulaire.jsp");
        mv.addObject("titre", "Formulaire GET et POST");
        mv.addObject("messageGet", "Données depuis GET: Initialisation " + System.currentTimeMillis());
        
    
        if (paramGet != null || nomGet != null || emailGet != null) {
            mv.addObject("donneesGet", "Paramètres GET reçus - paramGet: " + paramGet + ", nomGet: " + nomGet + ", emailGet: " + emailGet);
            mv.addObject("paramGet", paramGet);
            mv.addObject("nomGet", nomGet);
            mv.addObject("emailGet", emailGet);
            mv.addObject("timestampGet", new java.util.Date());
        }
        
        return mv;
    }

    @Url(value = "getgetrequet", method = "POST")
    public ModelView postGetRequet(String nom, String email) {
        System.out.println("🎯 Méthode POST - postGetRequet appelée avec: " + nom + ", " + email);
        
        ModelView mv = new ModelView("/formulaire.jsp");
        mv.addObject("titre", "Formulaire GET et POST - Résultat POST");
        mv.addObject("messageGet", "Données depuis GET: " + System.currentTimeMillis());
        
        // Données POST
        mv.addObject("messagePost", "Données POST reçues - Nom: " + nom + ", Email: " + email);
        mv.addObject("nom", nom);
        mv.addObject("email", email);
        mv.addObject("timestampPost", new java.util.Date());
        
        return mv;
    }
}