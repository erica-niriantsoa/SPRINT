package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPRINT 10 : Annotation pour mapper les fichiers uploadés
 * 
 * Exemple :
 *   @FileParam("photo") byte[] photo
 *   → Mappe le fichier uploadé avec name="photo" vers le paramètre photo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FileParam {
    String value();
    boolean required() default false;
}
