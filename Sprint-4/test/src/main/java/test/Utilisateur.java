package test;

import com.monframework.Url;

public class Utilisateur {
    
    @Url("merci")
    public String getMerciMonamie() {
        return "Merci mon amie ! ❤️<br>Je suis content que tu sois là.";
    }
    
    @Url("bonjour")
    public String direBonjour() {
        return "Bonjour tout le monde ! 👋<br>Bienvenue sur notre framework.";
    }
    
    @Url("test")
    public String methodeTest() {
        return "Ceci est un test réussi ! ✅<br>Le framework fonctionne parfaitement.";
    }
    
    @Url("info")
    public String getInfo() {
        return "Informations du système:<br>" +
               "- Framework: MonFramework<br>" +
               "- Date: " + new java.util.Date() + "<br>" +
               "- Utilisateur: Test";
    }
}