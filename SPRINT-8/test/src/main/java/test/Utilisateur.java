package test;

import java.util.Map;

import com.monframework.ModelView;
import com.monframework.Url;

public class Utilisateur {
    
    @Url("inscription")
    public ModelView choix(Map<String, Object> args) {
        // Extraction des valeurs depuis le Map
        String nom = (String) args.get("nom");
        
        // Pour les checkboxes, nous pouvons avoir plusieurs valeurs
        // Dans le Map, "matiere" pourrait être un String[] si plusieurs sont sélectionnées
        Object matiereObj = args.get("matiere");
        String matieres = "";
        
        if (matiereObj != null) {
            if (matiereObj instanceof String[]) {
                // Plusieurs matières sélectionnées
                String[] matieresArray = (String[]) matiereObj;
                matieres = String.join(", ", matieresArray);
            } else if (matiereObj instanceof String) {
                // Une seule matière sélectionnée
                matieres = (String) matiereObj;
            }
        }
        
        String age = (String) args.get("age");
        String email = (String) args.get("email");
        
        System.out.println("🎯 Méthode appelée avec:");
        System.out.println("👤 Nom: " + nom);
        System.out.println("📚 Matières: " + matieres);
        System.out.println("🗺️ Tous les arguments: " + args);
        
        ModelView mv = new ModelView("/result.jsp");
        mv.addObject("nom", nom);
        mv.addObject("matieres", matieres);
        mv.addObject("action", "inscription");
        mv.addObject("message", "Inscription réussie pour: " + nom);
        
        if (!matieres.isEmpty()) {
            mv.addObject("message", "Inscription réussie pour: " + nom + " en " + matieres);
        }
        
        // Si vous voulez aussi passer le Map complet à la vue
        mv.addObject("args", args);
        
        return mv;
    }
}