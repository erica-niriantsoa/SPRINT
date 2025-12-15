package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPRINT 9 : Annotation pour marquer les méthodes qui doivent retourner une réponse JSON
 * au lieu de faire un forward vers une vue JSP.
 * 
 * Utilisée pour créer des API REST.
 * 
 * Exemple :
 * <pre>
 * @Url("/api/employe/{id}")
 * @Get
 * @RestAPI
 * public Employe getEmploye(int id) {
 *     return employeService.findById(id);
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestAPI {
}
