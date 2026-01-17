package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPRINT 11 : Annotation pour injecter la session HTTP comme Map<String, Object>
 * 
 * Utilisation:
 *   @Get("/mon-url")
 *   public ModelAndView maMethode(@Session Map<String, Object> session) {
 *       // La session HTTP sera automatiquement convertie en Map et injectée
 *       String username = (String) session.get("username");
 *       session.put("lastVisit", new Date());
 *       ...
 *   }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Session {
}
