package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPRINT 11-bis : Annotation pour spécifier le(s) rôle(s) nécessaire(s) pour exécuter une méthode
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RoleRequired {
    String[] value() default {}; // Tableau de rôles autorisés (ex: {"chef", "admin"})
}
