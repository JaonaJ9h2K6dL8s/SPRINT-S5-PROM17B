package test;

import com.monframework.Controller;
import com.monframework.GetUrl;
import com.monframework.ModelView;
import com.monframework.Session;
import com.monframework.SessionMap;
import com.monframework.RequestParam;

@Controller
public class SessionController {

    @GetUrl(url = "/session-show")
    public ModelView showSession(@Session SessionMap session) {
        ModelView modelView = new ModelView("sessionView.jsp");
        modelView.addItem("session", session);
        return modelView;
    }

    @GetUrl(url = "/session-form")
    public ModelView sessionForm() {
        return new ModelView("sessionForm.jsp");
    }

    @GetUrl(url = "/session-add")
    public ModelView addToSession(
            @RequestParam(param = "key") String key,
            @RequestParam(param = "value") String value,
            @Session SessionMap session) {
        
        session.put(key, value);
        
        ModelView modelView = new ModelView("sessionResult.jsp");
        modelView.addItem("operation", "Ajout");
        modelView.addItem("key", key);
        modelView.addItem("value", value);
        modelView.addItem("session", session);
        return modelView;
    }

    @GetUrl(url = "/session-remove")
    public ModelView removeFromSession(
            @RequestParam(param = "key") String key,
            @Session SessionMap session) {
        
        String removedValue = (String) session.remove(key);
        
        ModelView modelView = new ModelView("sessionResult.jsp");
        modelView.addItem("operation", "Suppression");
        modelView.addItem("key", key);
        modelView.addItem("value", removedValue);
        modelView.addItem("session", session);
        return modelView;
    }

    @GetUrl(url = "/session-update")
    public ModelView updateSession(
            @RequestParam(param = "key") String key,
            @RequestParam(param = "value") String value,
            @Session SessionMap session) {
        
        String oldValue = (String) session.put(key, value);
        
        ModelView modelView = new ModelView("sessionResult.jsp");
        modelView.addItem("operation", "Modification");
        modelView.addItem("key", key);
        modelView.addItem("oldValue", oldValue);
        modelView.addItem("newValue", value);
        modelView.addItem("session", session);
        return modelView;
    }

    @GetUrl(url = "/session-get")
    public ModelView getFromSession(
            @RequestParam(param = "key") String key,
            @Session SessionMap session) {
        
        String value = (String) session.get(key);
        
        ModelView modelView = new ModelView("sessionResult.jsp");
        modelView.addItem("operation", "Consultation");
        modelView.addItem("key", key);
        modelView.addItem("value", value);
        modelView.addItem("session", session);
        return modelView;
    }

    @GetUrl(url = "/session-clear")
    public ModelView clearSession(@Session SessionMap session) {
        session.clear();
        
        ModelView modelView = new ModelView("sessionResult.jsp");
        modelView.addItem("operation", "Nettoyage complet");
        modelView.addItem("session", session);
        return modelView;
    }

    @GetUrl(url = "/session-invalidate")
    public ModelView invalidateSession(@Session SessionMap session) {
        session.invalidate();
        
        ModelView modelView = new ModelView("sessionResult.jsp");
        modelView.addItem("operation", "Invalidation");
        modelView.addItem("session", session);
        return modelView;
    }
}