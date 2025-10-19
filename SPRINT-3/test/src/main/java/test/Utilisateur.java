package test;

import com.monframework.Url;

public class Utilisateur {
    
    @Url("merci")
    public void getMerciMonamie() {
        System.out.println("Méthode getMerciMonamie exécutée !");
    }
    
    @Url("bonjour")
    public void direBonjour() {
        System.out.println("Méthode direBonjour exécutée !");
    }
    
    @Url("test")
    public void methodeTest() {
        System.out.println("Méthode test exécutée !");
    }
}
