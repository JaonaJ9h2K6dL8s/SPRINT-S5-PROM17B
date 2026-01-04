package com.monframework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "FrontServlet", urlPatterns = {"/", "/upload", "/list-files", "/upload-form"})
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class FrontServlet extends HttpServlet {
    private Map<String, Mapping> urlMappings = new HashMap<>();
    private String uploadPath;

    @Override
    public void init() throws ServletException {
        System.out.println("=== Initialisation de FrontServlet ===");
        
        // Définir le chemin d'upload
        this.uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        
        // Créer le répertoire uploads s'il n'existe pas
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("Dossier uploads créé: " + uploadPath);
        }
        
        System.out.println("Chemin d'upload: " + uploadPath);
        
        // Scanner les contrôleurs
        try {
            scanControllers("test");
        } catch (Exception e) {
            System.err.println("Erreur lors du scan des contrôleurs: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Mappings chargés: " + urlMappings.size());
        for (String key : urlMappings.keySet()) {
            System.out.println("  " + key + " -> " + urlMappings.get(key).getMethod().getName());
        }
    }

    private void scanControllers(String packageName) {
        try {
            System.out.println("Scan du package: " + packageName);
            
            // Essayer de charger la classe UploadController
            Class<?> controllerClass = null;
            try {
                controllerClass = Class.forName(packageName + ".UploadController");
                System.out.println("Classe chargée: " + controllerClass.getName());
            } catch (ClassNotFoundException e) {
                // Essayer avec controllers
                controllerClass = Class.forName(packageName + ".controllers.UploadController");
                System.out.println("Classe chargée: " + controllerClass.getName());
            }
            
            // Analyser les méthodes avec l'annotation @Url
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Url.class)) {
                    Url urlAnnotation = method.getAnnotation(Url.class);
                    String url = urlAnnotation.value();
                    
                    // Assurer que l'URL commence par /
                    if (!url.startsWith("/")) {
                        url = "/" + url;
                    }
                    
                    urlMappings.put(url, new Mapping(controllerClass, method));
                    System.out.println("Mapping ajouté: " + url + " -> " + method.getName());
                }
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("Classe non trouvée: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur lors du scan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Log de débogage
        String fullUrl = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = fullUrl.substring(contextPath.length());
        
        System.out.println("\n=== Nouvelle requête ===");
        System.out.println("Méthode: " + request.getMethod());
        System.out.println("URL complète: " + fullUrl);
        System.out.println("Context Path: " + contextPath);
        System.out.println("URL relative: " + url);
        
        // Gestion de la racine
        if (url.equals("") || url.equals("/")) {
            System.out.println("Requête racine détectée, redirection vers /upload-form");
            // Rediriger vers upload-form
            response.sendRedirect(contextPath + "/upload-form");
            return;
        }
        
        // Chercher le mapping correspondant
        Mapping mapping = urlMappings.get(url);
        
        if (mapping == null) {
            System.err.println("Aucun mapping trouvé pour: " + url);
            System.out.println("Mappings disponibles: " + urlMappings.keySet());
            
            // Si c'est une URL directe vers un JSP, laisser Tomcat gérer
            if (url.endsWith(".jsp")) {
                System.out.println("URL JSP, forward direct");
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                return;
            }
            
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: " + url);
            return;
        }

        System.out.println("Mapping trouvé: " + mapping.getMethod().getName());
        
        try {
            // Créer une instance du contrôleur
            Object controller = mapping.getControllerClass().getDeclaredConstructor().newInstance();
            
            // Préparer les paramètres
            Object[] params = prepareParameters(request, response, mapping.getMethod());
            
            // Appeler la méthode
            System.out.println("Appel de la méthode: " + mapping.getMethod().getName());
            Object result = mapping.getMethod().invoke(controller, params);
            
            // Traiter le résultat
            if (result instanceof ModelView) {
                ModelView mv = (ModelView) result;
                System.out.println("ModelView retourné, vue: " + mv.getView());
                
                // Ajouter les données à la requête
                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                    System.out.println("Attribut ajouté: " + entry.getKey() + " = " + entry.getValue());
                }
                
                // Rediriger vers la vue
                String viewPath = mv.getView();
                if (!viewPath.startsWith("/")) {
                    viewPath = "/" + viewPath;
                }
                
                System.out.println("Forward vers: " + viewPath);
                RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
                dispatcher.forward(request, response);
                
            } else if (result instanceof String) {
                // Redirection simple
                String redirectUrl = (String) result;
                System.out.println("Redirection vers: " + redirectUrl);
                if (!redirectUrl.startsWith("http") && !redirectUrl.startsWith("/")) {
                    redirectUrl = contextPath + "/" + redirectUrl;
                } else if (!redirectUrl.startsWith("http")) {
                    redirectUrl = contextPath + redirectUrl;
                }
                response.sendRedirect(redirectUrl);
                
            } else {
                System.err.println("Type de retour non géré: " + result.getClass());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Type de retour non géré: " + result.getClass());
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Erreur: " + e.getMessage());
        }
    }

    private Object[] prepareParameters(HttpServletRequest request, HttpServletResponse response, Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        
        System.out.println("Préparation des paramètres pour " + method.getName());
        System.out.println("Nombre de paramètres: " + paramTypes.length);
        
        for (int i = 0; i < paramTypes.length; i++) {
            System.out.println("  Paramètre " + i + ": " + paramTypes[i].getName());
            
            if (paramTypes[i].equals(HttpServletRequest.class)) {
                params[i] = request;
                System.out.println("    -> HttpServletRequest");
            } else if (paramTypes[i].equals(HttpServletResponse.class)) {
                params[i] = response;
                System.out.println("    -> HttpServletResponse");
            } else if (paramTypes[i].equals(FileUpload.class)) {
                params[i] = handleFileUpload(request);
                System.out.println("    -> FileUpload: " + (params[i] != null ? "oui" : "null"));
            } else if (paramTypes[i].equals(String.class)) {
                // Chercher un paramètre avec un nom spécifique
                String[] paramNames = {"nom", "description", "action"};
                for (String paramName : paramNames) {
                    String value = request.getParameter(paramName);
                    if (value != null) {
                        params[i] = value;
                        System.out.println("    -> String: " + paramName + " = " + value);
                        break;
                    }
                }
            } else {
                System.out.println("    -> null (type non géré)");
                params[i] = null;
            }
        }
        
        return params;
    }

    private FileUpload handleFileUpload(HttpServletRequest request) {
        try {
            String contentType = request.getContentType();
            if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
                System.out.println("Pas une requête multipart");
                return null;
            }
            
            Collection<Part> parts = request.getParts();
            System.out.println("Nombre de parts: " + parts.size());
            
            for (Part part : parts) {
                System.out.println("Part: " + part.getName() + ", taille: " + part.getSize());
                
                if (part.getName().equals("fichier") && part.getSize() > 0) {
                    String fileName = extractFileName(part);
                    System.out.println("Fichier détecté: " + fileName);
                    
                    // Lire les données
                    InputStream fileContent = part.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileContent.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, bytesRead);
                    }
                    buffer.flush();
                    
                    byte[] fileData = buffer.toByteArray();
                    
                    // Sauvegarder le fichier
                    String savedPath = saveFileToDisk(part, fileName);
                    System.out.println("Fichier sauvegardé: " + savedPath);
                    
                    // Créer l'objet FileUpload
                    FileUpload fileUpload = new FileUpload(fileData, fileName, part.getSize());
                    return fileUpload;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'upload: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) return "unknown";
        
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String fileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                // Garder uniquement le nom du fichier
                return fileName.substring(fileName.lastIndexOf("\\") + 1);
            }
        }
        return "unknown";
    }

    private String saveFileToDisk(Part part, String fileName) throws IOException {
        String filePath = uploadPath + File.separator + fileName;
        part.write(filePath);
        return filePath;
    }

    // Classe interne pour les mappings
    private static class Mapping {
        private Class<?> controllerClass;
        private Method method;

        public Mapping(Class<?> controllerClass, Method method) {
            this.controllerClass = controllerClass;
            this.method = method;
        }

        public Class<?> getControllerClass() {
            return controllerClass;
        }

        public Method getMethod() {
            return method;
        }
    }
}