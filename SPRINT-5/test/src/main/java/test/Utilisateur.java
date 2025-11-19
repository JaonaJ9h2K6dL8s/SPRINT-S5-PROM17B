package test;

import com.monframework.ModelView;
import com.monframework.Url;

public class Utilisateur {
        @Url("merci")
        public ModelView getMerciMonamie() {
            ModelView mv = new ModelView("/merci.jsp");
            mv.setData("message", "Merci mon amie ❤️");
            return mv;
        }

        @Url("bonjour")
        public ModelView direBonjour() {
            ModelView mv = new ModelView("/bonjour.jsp");
            mv.setData("texte", "Bonjour tout le monde ! 👋 Bienvenue sur notre framework.");
            return mv;
        }
}