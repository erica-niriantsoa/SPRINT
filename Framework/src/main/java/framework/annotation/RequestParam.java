package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPRINT 6-ter : Annotation pour mapper explicitement les query params
 * 
 * Exemple :
 *   @RequestParam("student_id") int id
 *   → Mappe ?student_id=123 vers le parametre id
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();
    boolean required() default true;
}