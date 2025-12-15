package test;

import com.monframework.Controller;
import com.monframework.GetUrl;
import com.monframework.ModelView;
import com.monframework.PostUrl;
import com.monframework.RequestParam;

@Controller("/eleve")
public class EleveController {
    

    @GetUrl("/form")
    public ModelView showForm() {
        ModelView mv = new ModelView();
        mv.setView("formEleve.jsp");
        return mv;
    }
    

    @PostUrl("/save")
    public ModelView saveEleve(Eleve eleve) {
        System.out.println("=== ELEVE REÇU ===");
        System.out.println(eleve);
        
        ModelView mv = new ModelView();
        mv.setView("resultEleve.jsp");
        mv.setData("eleve", eleve);
        mv.setData("message", "Élève créé avec succès");
        
        return mv;
    }
    

    @PostUrl("/save2")
    public ModelView saveEleve2(
            @RequestParam("nom") String nom,
            @RequestParam("age") int age,
            @RequestParam("matiere") String matiere,
            @RequestParam("valeur") double valeur) {
        
        System.out.println("=== DONNÉES REÇUES ===");
        System.out.println("Nom: " + nom + ", Age: " + age);
        System.out.println("Matière: " + matiere + ", Note: " + valeur);
        
     
        Eleve eleve = new Eleve();
        eleve.setNom(nom);
        eleve.setAge(age);
        
        Note note = new Note();
        note.setMatiere(matiere);
        note.setValeur(valeur);
        eleve.setNote(note);
        
        ModelView mv = new ModelView();
        mv.setView("resultEleve.jsp");
        mv.setData("eleve", eleve);
        mv.setData("message", "Élève créé (méthode RequestParam)");
        
        return mv;
    }
    
}