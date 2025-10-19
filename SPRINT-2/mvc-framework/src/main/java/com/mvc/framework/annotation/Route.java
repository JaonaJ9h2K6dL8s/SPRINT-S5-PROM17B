package com.mvc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour mapper une méthode de contrôleur à une URL spécifique.
 * Les méthodes annotées avec @Route seront automatiquement appelées
 * lorsque l'URL correspondante est demandée.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
    /**
     * L'URL à mapper à cette méthode.
     * Exemple: @Route(url="/home") ou @Route("/users")
     */
    String url();
    
    /**
     * Méthode HTTP supportée (GET, POST, etc.).
     * Par défaut, toutes les méthodes sont acceptées.
     */
    String method() default "";
}