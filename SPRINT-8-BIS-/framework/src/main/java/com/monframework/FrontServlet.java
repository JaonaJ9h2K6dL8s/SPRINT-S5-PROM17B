package com.monframework;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontServlet extends HttpServlet {

    private static final List<RouteMapping> routes = new ArrayList<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static boolean initialized = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        servicePersonnalisee(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        servicePersonnalisee(req, resp);
    }

    private void servicePersonnalisee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!initialized) {
            System.out.println("INITIALISATION : scan des routes...");
            scanRoutes();
            System.out.println("ROUTES DÉTECTÉES : " + routes.size());
            initialized = true;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUri = fullUri.substring(contextPath.length());
        if (relativeUri.startsWith("/")) relativeUri = relativeUri.substring(1);
        String routePath = "/" + relativeUri;
        String normalized = normalizePath(routePath);
        String currentMethod = request.getMethod();

        System.out.println("URL demandée : " + routePath + " → normalisé : " + normalized);

        // Recherche de la route correspondante
        for (RouteMapping route : routes) {
            if (route.getPath().equals(normalized) && 
                (route.getHttpMethod().equals("ANY") || route.getHttpMethod().equals(currentMethod))) {
                executeRoute(route, request, response);
                return;
            }
        }

        // Si pas de route trouvée, afficher une page simple
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>FrontServlet</title></head><body>");
            out.println("<h1>Etu003321</h1>");
            out.println("<p><a href='/sprint8bis-core/eleve/form'>Accéder au formulaire Élève</a></p>");
            out.println("</body></html>");
        }
    }

    private Object createAndPopulateObject(Class<?> clazz, HttpServletRequest request) throws Exception {
        Object obj = clazz.getDeclaredConstructor().newInstance();
        
        // Parcourir tous les paramètres de la requête
        Enumeration<String> paramNames = request.getParameterNames();
        
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            
            if (paramValue != null && !paramValue.isEmpty()) {
                // Gérer les paramètres imbriqués : note.matiere, note.valeur
                if (paramName.contains(".")) {
                    String[] parts = paramName.split("\\.");
                    String objectName = parts[0]; // "note"
                    String fieldName = parts[1]; // "matiere" ou "valeur"
                    
                    try {
                        // Getter pour l'objet imbriqué
                        String getterName = "get" + objectName.substring(0, 1).toUpperCase() + 
                                        objectName.substring(1);
                        Method getter = clazz.getMethod(getterName);
                        Object nestedObj = getter.invoke(obj);
                        
                        // Si l'objet imbriqué n'existe pas encore, le créer
                        if (nestedObj == null) {
                            Class<?> nestedType = getter.getReturnType();
                            nestedObj = nestedType.getDeclaredConstructor().newInstance();
                            
                            // Setter pour initialiser l'objet imbriqué
                            String setterName = "set" + objectName.substring(0, 1).toUpperCase() + 
                                            objectName.substring(1);
                            Method setter = clazz.getMethod(setterName, nestedType);
                            setter.invoke(obj, nestedObj);
                        }
                        
                        // Définir la valeur sur l'objet imbriqué
                        setFieldOnObject(nestedObj, fieldName, paramValue);
                        
                    } catch (Exception e) {
                        System.err.println("Erreur paramètre imbriqué " + paramName + ": " + e.getMessage());
                    }
                } else {
                    // Paramètre simple
                    setFieldOnObject(obj, paramName, paramValue);
                }
            }
        }
        
        return obj;
    }

    private void setFieldOnObject(Object obj, String fieldName, String value) {
        try {
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + 
                            fieldName.substring(1);
            
            // Chercher le setter
            for (Method method : obj.getClass().getMethods()) {
                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Object convertedValue = convertValue(value, paramType);
                    
                    if (convertedValue != null) {
                        method.invoke(obj, convertedValue);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur setFieldOnObject " + fieldName + ": " + e.getMessage());
        }
    }

    private Object convertValue(String value, Class<?> targetType) {
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            System.err.println("Erreur conversion " + value + " vers " + targetType.getName());
        }
        return null;
    }

    private void executeRoute(RouteMapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Method method = mapping.getMethod();
        Object controller = controllers.get(method.getDeclaringClass().getName());
        
        try {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Class<?> paramType = param.getType();

                System.out.println("Traitement paramètre " + i + ": " + param.getName() + 
                                " de type: " + paramType.getName());

                // 1. SI C'EST UN OBJET COMPLEXE (Eleve, Note, etc.)
                if (!paramType.isPrimitive() && 
                    !paramType.isArray() && 
                    !paramType.equals(String.class) &&
                    !Number.class.isAssignableFrom(paramType) &&
                    !paramType.equals(Boolean.class)) {
                    
                    try {
                        System.out.println("Tentative création objet: " + paramType.getName());
                        Object obj = createAndPopulateObject(paramType, request);
                        args[i] = obj;
                        System.out.println("Objet créé avec succès: " + obj);
                        continue;
                    } catch (Exception e) {
                        System.err.println("Erreur création objet " + paramType.getName() + ": " + e.getMessage());
                        args[i] = null;
                        continue;
                    }
                }

                // 2. SI C'EST UN PARAMÈTRE SIMPLE (@RequestParam)
                RequestParam rp = param.getAnnotation(RequestParam.class);
                String value = null;
                
                if (rp != null && !rp.value().isEmpty()) {
                    value = request.getParameter(rp.value());
                    System.out.println("RequestParam trouvé: " + rp.value() + " = " + value);
                } else {
                    // Par nom de paramètre
                    String paramName = param.getName();
                    value = request.getParameter(paramName);
                    System.out.println("Paramètre par nom: " + paramName + " = " + value);
                }
                
                if (value == null || value.isEmpty()) {
                    if (rp != null && !rp.value().isEmpty()) {
                        request.setAttribute("error", "incorrecte");
                        request.setAttribute("errorMessage", "Le champ <strong>name=\"" + rp.value() + "\"</strong> est manquant.");
                        request.getRequestDispatcher("/resultat.jsp").forward(request, response);
                        return;
                    }
                    value = "";
                }
                
                // Convertir la valeur selon le type du paramètre
                if (paramType == String.class) {
                    args[i] = value;
                } else if (paramType == int.class || paramType == Integer.class) {
                    try {
                        args[i] = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        args[i] = 0;
                    }
                } else if (paramType == double.class || paramType == Double.class) {
                    try {
                        String normalizedValue = value.replace(',', '.');
                        args[i] = Double.parseDouble(normalizedValue);
                    } catch (NumberFormatException e) {
                        args[i] = 0.0;
                    }
                } else if (paramType == boolean.class || paramType == Boolean.class) {
                    args[i] = Boolean.parseBoolean(value);
                } else {
                    args[i] = value;
                }
            }

            // Appeler la méthode du contrôleur
            System.out.println("Appel de la méthode: " + method.getName());
            Object result = method.invoke(controller, args);
            
            // Gérer le résultat
            handleResult(result, request, response);
            
        } catch (Exception e) {
            System.err.println("Erreur dans executeRoute: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().println("<h1 style='color:red'>Erreur : " + e.getMessage() + "</h1>");
        }
    }

    private void handleResult(Object result, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (result instanceof ModelView) {
            ModelView mv = (ModelView) result;
            String viewPath = "/" + mv.getView();
            
            // Ajouter les données du ModelView à la requête
            for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            
            // Transmettre à la JSP
            request.getRequestDispatcher(viewPath).forward(request, response);
        }
        else {
            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><head><title>Résultat</title></head><body>");
                out.println("<h1>" + result + "</h1>");
                out.println("</body></html>");
            }
        }
    }

    private String normalizePath(String path) {
        return path.replaceAll("/+", "/").replaceAll("/$", "");
    }

    private void scanRoutes() {
        try {
            String packageName = "test";
            String path = packageName.replace('.', '/');
            URL resource = getServletContext().getClassLoader().getResource(path);
            if (resource == null) return;

            File dir = new File(resource.toURI());
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> cls = Class.forName(className);
                    if (cls.isAnnotationPresent(Controller.class)) {
                        Controller controller = cls.getAnnotation(Controller.class);
                        String basePath = controller.value();
                        Object instance = cls.getDeclaredConstructor().newInstance();
                        controllers.put(cls.getName(), instance);

                        for (Method method : cls.getDeclaredMethods()) {
                            String fullPath;

                            if (method.isAnnotationPresent(GetUrl.class)) {
                                fullPath = normalizePath(basePath + method.getAnnotation(GetUrl.class).value());
                                routes.add(new RouteMapping("GET", fullPath, method, instance));
                            }
                            if (method.isAnnotationPresent(PostUrl.class)) {
                                fullPath = normalizePath(basePath + method.getAnnotation(PostUrl.class).value());
                                routes.add(new RouteMapping("POST", fullPath, method, instance));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}