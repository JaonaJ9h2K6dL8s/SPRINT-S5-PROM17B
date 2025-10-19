package com.mvc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour marquer une classe comme contrôleur MVC.
 * Les classes annotées avec @Controller seront automatiquement
 * détectées par le framework et leurs méthodes @Route seront mappées.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    /**
     * Nom optionnel du contrôleur.
     * Si non spécifié, le nom de la classe sera utilisé.
     */
    String value() default "";
}