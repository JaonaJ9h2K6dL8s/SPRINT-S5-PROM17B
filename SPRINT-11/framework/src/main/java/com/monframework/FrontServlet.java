package com.monframework;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class FrontServlet extends HttpServlet {
    private Map<String, Mapping> urlMappings = new HashMap<>();
    
    @Override
    public void init() throws ServletException {
        try {
            // Scanner les contrôleurs
            List<Class<?>> controllers = findControllers("test");
            for (Class<?> controller : controllers) {
                Method[] methods = controller.getDeclaredMethods();
                for (Method method : methods) {
                    GetUrl getUrl = method.getAnnotation(GetUrl.class);
                    if (getUrl != null) {
                        String url = getUrl.url();
                        urlMappings.put(url, new Mapping(controller, method));
                        System.out.println("[FrontServlet] Mapping URL: " + url + " -> " + 
                                          controller.getName() + "." + method.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String url = request.getRequestURI().substring(request.getContextPath().length());
        
        System.out.println("[FrontServlet] URL demandée: " + url);
        
        Mapping mapping = urlMappings.get(url);
        if (mapping == null) {
            System.out.println("[FrontServlet] URL non trouvée: " + url);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        try {
            // Créer l'instance du contrôleur
            Object controller = mapping.controllerClass.getDeclaredConstructor().newInstance();
            
            // Préparer les paramètres de la méthode
            Parameter[] parameters = mapping.method.getParameters();
            Object[] args = new Object[parameters.length];
            
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                
                // Gérer @RequestParam
                RequestParam requestParam = param.getAnnotation(RequestParam.class);
                if (requestParam != null) {
                    String paramName = requestParam.param();
                    String paramValue = request.getParameter(paramName);
                    System.out.println("[FrontServlet] Paramètre " + paramName + " = " + paramValue);
                    args[i] = paramValue;
                }
                
                // Gérer @Session
                Session sessionAnnotation = param.getAnnotation(Session.class);
                if (sessionAnnotation != null) {
                    HttpSession httpSession = request.getSession(true);
                    System.out.println("[FrontServlet] Session ID: " + httpSession.getId());
                    
                    // Créer le SessionMap avec la session HTTP
                    SessionMap sessionMap = new SessionMap(httpSession);
                    args[i] = sessionMap;
                    
                    // Debug: Afficher les attributs de session
                    Enumeration<String> attributeNames = httpSession.getAttributeNames();
                    System.out.println("[FrontServlet] Attributs de session:");
                    while (attributeNames.hasMoreElements()) {
                        String attr = attributeNames.nextElement();
                        System.out.println("  - " + attr + " = " + httpSession.getAttribute(attr));
                    }
                }
            }
            
            // Appeler la méthode du contrôleur
            System.out.println("[FrontServlet] Appel de " + mapping.method.getName());
            ModelView modelView = (ModelView) mapping.method.invoke(controller, args);
            
            // Mettre à jour la session HTTP avec les données du SessionMap
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Session sessionAnnotation = param.getAnnotation(Session.class);
                if (sessionAnnotation != null && args[i] instanceof SessionMap) {
                    SessionMap sessionMap = (SessionMap) args[i];
                    // Les données sont déjà dans la session HTTP via SessionMap
                }
            }
            
            // Passer les données à la JSP
            Map<String, Object> data = modelView.getData();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
                System.out.println("[FrontServlet] Attribut request: " + entry.getKey() + " = " + entry.getValue());
            }
            
            // Rediriger vers la vue
            String viewPath = modelView.getView();
            System.out.println("[FrontServlet] Forward vers: " + viewPath);
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur dans FrontServlet: " + e.getMessage(), e);
        }
    }
    
    private List<Class<?>> findControllers(String packageName) throws Exception {
        List<Class<?>> controllers = new ArrayList<>();
        
        try {
            Class<?> sessionController = Class.forName("test.SessionController");
            if (sessionController.isAnnotationPresent(Controller.class)) {
                System.out.println("[FrontServlet] Contrôleur trouvé: " + sessionController.getName());
                controllers.add(sessionController);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[FrontServlet] Classe non trouvée: test.SessionController");
            throw e;
        }
        
        return controllers;
    }
    
    private static class Mapping {
        Class<?> controllerClass;
        Method method;
        
        Mapping(Class<?> controllerClass, Method method) {
            this.controllerClass = controllerClass;
            this.method = method;
        }
    }
}