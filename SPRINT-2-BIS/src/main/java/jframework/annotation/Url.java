package jframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour mapper une méthode à une URL spécifique
 * Utilisée dans le framework pour définir les routes
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Url {
    /**
     * L'URL à mapper à la méthode
     * @return l'URL sous forme de chaîne
     */
    String value();
}