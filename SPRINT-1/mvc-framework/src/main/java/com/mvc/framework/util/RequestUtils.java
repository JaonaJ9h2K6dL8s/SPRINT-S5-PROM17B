package com.mvc.framework.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitaire pour manipuler les requêtes HTTP
 * 
 * @author MVC Framework Team
 * @version 1.0.0
 */
public class RequestUtils {
    
    /**
     * Extrait tous les paramètres de la requête sous forme de Map
     * 
     * @param request La requête HTTP
     * @return Map contenant tous les paramètres
     */
    public static Map<String, String> getParametersAsMap(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                parameters.put(key, values[0]); // Prend la première valeur
            }
        }
        
        return parameters;
    }
    
    /**
     * Extrait tous les headers de la requête sous forme de Map
     * 
     * @param request La requête HTTP
     * @return Map contenant tous les headers
     */
    public static Map<String, String> getHeadersAsMap(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        
        return headers;
    }
    
    /**
     * Vérifie si la requête est une requête AJAX
     * 
     * @param request La requête HTTP
     * @return true si c'est une requête AJAX, false sinon
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
    
    /**
     * Obtient l'adresse IP réelle du client (en tenant compte des proxies)
     * 
     * @param request La requête HTTP
     * @return L'adresse IP du client
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * Construit l'URL complète de la requête
     * 
     * @param request La requête HTTP
     * @return L'URL complète
     */
    public static String getFullRequestURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}