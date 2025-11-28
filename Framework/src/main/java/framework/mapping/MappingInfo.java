package framework.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SPRINT 6 + SPRINT 7 : Classe qui represente un mapping URL → Methode
 * 
 * Cette classe stocke :
 * - SPRINT 6 : Pattern URL avec variables {id}
 * - SPRINT 7 : Methode HTTP (GET, POST)
 * - Extraction des parametres URL
 */
public class MappingInfo {
    
    private Class<?> controllerClass;
    private Method method;
    private String urlPattern;
    private String httpMethod;  // SPRINT 7 : GET ou POST
    
    // SPRINT 6 : Pour les patterns avec variables
    private Pattern pattern;
    private List<String> paramNames;
    private Map<String, String> urlParams;
    
    /**
     * Constructeur
     * 
     * @param controllerClass La classe du controleur
     * @param method La methode a invoquer
     * @param urlPattern Le pattern URL (ex: /etudiant/{id})
     * @param httpMethod La methode HTTP (GET ou POST)
     */
    public MappingInfo(Class<?> controllerClass, Method method, String urlPattern, String httpMethod) {
        this.controllerClass = controllerClass;
        this.method = method;
        this.urlPattern = urlPattern;
        this.httpMethod = httpMethod;
        this.urlParams = new HashMap<>();
        
        // SPRINT 6 : Compiler le pattern si contient des variables
        if (urlPattern.contains("{")) {
            compilePattern(urlPattern);
        }
    }
    
    /**
     * SPRINT 6 : Compile un pattern URL en regex
     * 
     * Exemple :
     *   /etudiant/{id}/notes/{matiere}
     *   → Regex: ^/etudiant/([^/]+)/notes/([^/]+)$
     *   → paramNames: ["id", "matiere"]
     */
    private void compilePattern(String urlPattern) {
        paramNames = new ArrayList<>();
        String regex = urlPattern;
        
        // Extraire les noms de parametres entre accolades
        Pattern paramPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = paramPattern.matcher(urlPattern);
        
        while (matcher.find()) {
            paramNames.add(matcher.group(1)); // group(1) = contenu entre {}
        }
        
        // Convertir {id} en regex ([^/]+) qui matche tout sauf /
        regex = regex.replaceAll("\\{[^}]+\\}", "([^/]+)");
        pattern = Pattern.compile("^" + regex + "$");
    }
    
    /**
     * SPRINT 6 : Verifie si une URL concrete matche ce pattern
     * et extrait les valeurs des variables
     * 
     * Exemple :
     *   Pattern: /etudiant/{id}/notes/{matiere}
     *   URL:     /etudiant/17/notes/Math
     *   Retour:  {"id": "17", "matiere": "Math"}
     * 
     * @param url L'URL concrete a tester
     * @return Map des parametres extraits, ou null si pas de match
     */
    public Map<String, String> matchUrl(String url) {
        if (pattern == null) {
            return null;
        }
        
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < paramNames.size(); i++) {
                params.put(paramNames.get(i), matcher.group(i + 1));
            }
            return params;
        }
        return null;
    }
    
    /**
     * Verifie si ce mapping correspond a une URL et methode HTTP
     * 
     * @param url L'URL a tester
     * @param httpMethod La methode HTTP (GET, POST)
     * @return true si correspond
     */
    public boolean matches(String url, String httpMethod) {
        if (!this.httpMethod.equals(httpMethod)) {
            return false;
        }
        
        // Si pattern avec variables
        if (pattern != null) {
            return matchUrl(url) != null;
        }
        
        // Sinon URL exacte
        return this.urlPattern.equals(url);
    }
    
    // ========================================================================
    // GETTERS ET SETTERS
    // ========================================================================
    
    public Class<?> getControllerClass() { 
        return controllerClass; 
    }
    
    public Method getMethod() { 
        return method; 
    }
    
    public String getUrlPattern() { 
        return urlPattern; 
    }
    
    public String getHttpMethod() { 
        return httpMethod; 
    }
    
    public Map<String, String> getUrlParams() { 
        return urlParams; 
    }
    
    public void setUrlParams(Map<String, String> params) { 
        this.urlParams = params; 
    }
    
    public List<String> getParamNames() {
        return paramNames;
    }
    
    public boolean isPattern() {
        return pattern != null;
    }
    
    // ========================================================================
    // DEBUG / AFFICHAGE
    // ========================================================================
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(urlPattern)
          .append(" [").append(httpMethod).append("]")
          .append(" → ")
          .append(controllerClass.getSimpleName())
          .append(".")
          .append(method.getName());
        
        if (isPattern()) {
            sb.append(" (pattern)");
        }
        
        return sb.toString();
    }
}