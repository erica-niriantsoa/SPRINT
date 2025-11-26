package framework.scanner;

import framework.annotation.Controller;
import framework.annotation.Url;
import framework.annotation.scanner.ClassPathScanner;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.servlet.ServletContext;

/**
 * SPRINT 6 : Scanner qui detecte les URLs avec variables {id}
 * et les URLs normales
 */
public class AnnotationScanner {
    
    // Map pour les URLs exactes (sans variables)
    private Map<String, MappingInfo> urlMappings = new HashMap<>();
    
    // SPRINT 6 : List pour les URLs avec patterns/variables
    private List<MappingInfo> patternMappings = new ArrayList<>();
    
    private ClassPathScanner classPathScanner = new ClassPathScanner();
    private ServletContext servletContext;
    
    public AnnotationScanner(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public AnnotationScanner() {
        this.servletContext = null;
    }

    public void initialize() {
        System.out.println(" Initialisation du scanner d'annotations...");
        
        String[] packagesToScan = {"test.controller", "test"};
        
        for (String packageName : packagesToScan) {
            List<Class<?>> controllers = classPathScanner.findAnnotatedClasses(packageName, Controller.class);
            
            for (Class<?> controllerClass : controllers) {
                mapControllerUrls(controllerClass);
            }
        }
        
        System.out.println(" " + (urlMappings.size() + patternMappings.size()) + " URL(s) mappee(s)");
        displayMappedUrls();

        storeMappingsInContext();
    }
    
    private void storeMappingsInContext() {
        if (servletContext != null) {
            servletContext.setAttribute("framework.urlMappings", urlMappings);
            servletContext.setAttribute("framework.patternMappings", patternMappings);
            
            List<String> allUrls = new ArrayList<>(urlMappings.keySet());
            for (MappingInfo mapping : patternMappings) {
                allUrls.add(mapping.getUrlPattern());
            }
            servletContext.setAttribute("framework.mappedUrls", allUrls);
            System.out.println("  Mappings stockes dans ServletContext");
        }
    }
    
    private void mapControllerUrls(Class<?> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Url.class)) {
                Url urlAnnotation = method.getAnnotation(Url.class);
                String url = urlAnnotation.value();

                String cleanUrl = removeQueryParams(url);
                
                // SPRINT 6 : Detection des URLs avec variables {id}
                if (cleanUrl.contains("{") && cleanUrl.contains("}")) {
                    MappingInfo mapping = new MappingInfo(controllerClass, method, cleanUrl);
                    patternMappings.add(mapping);
                    System.out.println(" " + cleanUrl + " (pattern) → " + 
                        controllerClass.getSimpleName() + "." + method.getName());
                } else {
                    urlMappings.put(cleanUrl, new MappingInfo(controllerClass, method, cleanUrl));
                    System.out.println(" " + cleanUrl + " → " + 
                        controllerClass.getSimpleName() + "." + method.getName());
                }
            }
        }
    }

    private String removeQueryParams(String url) {
        if (url.contains("?")) {
            return url.substring(0, url.indexOf("?"));
        }
        return url;
    }
    
    public MappingInfo getMapping(String url) {
        // D'abord chercher une correspondance exacte
        if (urlMappings.containsKey(url)) {
            return urlMappings.get(url);
        }
        
        // SPRINT 6 : Chercher dans les patterns
        for (MappingInfo mapping : patternMappings) {
            Map<String, String> params = mapping.matchUrl(url);
            if (params != null) {
                mapping.setUrlParams(params);
                return mapping;
            }
        }
        
        return null;
    }
    
    public void displayMappedUrls() {
        System.out.println(" URLs supportees:");
        for (String url : urlMappings.keySet()) {
            MappingInfo mapping = urlMappings.get(url);
            System.out.println("  " + url + " → " + 
                mapping.getControllerClass().getSimpleName() + "." + mapping.getMethod().getName());
        }
        for (MappingInfo mapping : patternMappings) {
            System.out.println("  " + mapping.getUrlPattern() + " (pattern) → " + 
                mapping.getControllerClass().getSimpleName() + "." + mapping.getMethod().getName());
        }
    }
    
    public void displayMappedUrlsInHTML(PrintWriter out) {
        for (String url : urlMappings.keySet()) {
            MappingInfo mapping = urlMappings.get(url);
            out.println("<li><strong>" + url + "</strong> → " +
                mapping.getControllerClass().getSimpleName() + "." + mapping.getMethod().getName() + "</li>");
        }
        for (MappingInfo mapping : patternMappings) {
            out.println("<li><strong>" + mapping.getUrlPattern() + "</strong> (pattern) → " +
                mapping.getControllerClass().getSimpleName() + "." + mapping.getMethod().getName() + "</li>");
        }
    }
    
    /**
     * SPRINT 6 : Classe qui gere les patterns URL avec variables
     */
    public static class MappingInfo {
        private Class<?> controllerClass;
        private Method method;
        private String urlPattern;
        private Pattern pattern;
        private List<String> paramNames;
        private Map<String, String> urlParams;
        
        public MappingInfo(Class<?> controllerClass, Method method, String urlPattern) {
            this.controllerClass = controllerClass;
            this.method = method;
            this.urlPattern = urlPattern;
            this.urlParams = new HashMap<>();
            
            if (urlPattern.contains("{")) {
                compilePattern(urlPattern);
            }
        }
        
        /**
         * SPRINT 6 : Compile /etudiant/{id} en regex ^/etudiant/([^/]+)$
         */
        private void compilePattern(String urlPattern) {
            paramNames = new ArrayList<>();
            String regex = urlPattern;
            
            Pattern paramPattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher matcher = paramPattern.matcher(urlPattern);
            
            while (matcher.find()) {
                paramNames.add(matcher.group(1));
            }
            
            regex = regex.replaceAll("\\{[^}]+\\}", "([^/]+)");
            pattern = Pattern.compile("^" + regex + "$");
        }
        
        /**
         * SPRINT 6 : Extrait les valeurs des variables
         * /etudiant/17 → {"id": "17"}
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
        
        public Class<?> getControllerClass() { return controllerClass; }
        public Method getMethod() { return method; }
        public String getUrlPattern() { return urlPattern; }
        public Map<String, String> getUrlParams() { return urlParams; }
        public void setUrlParams(Map<String, String> params) { this.urlParams = params; }
    }

    public List<String> getMappedUrls() {
        List<String> allUrls = new ArrayList<>(urlMappings.keySet());
        for (MappingInfo mapping : patternMappings) {
            allUrls.add(mapping.getUrlPattern());
        }
        return allUrls;
    }
    
    public static MappingInfo getMappingFromContext(ServletContext context, String url) {
        Map<String, MappingInfo> mappings = (Map<String, MappingInfo>) context.getAttribute("framework.urlMappings");
        if (mappings != null && mappings.containsKey(url)) {
            return mappings.get(url);
        }
        
        List<MappingInfo> patterns = (List<MappingInfo>) context.getAttribute("framework.patternMappings");
        if (patterns != null) {
            for (MappingInfo mapping : patterns) {
                Map<String, String> params = mapping.matchUrl(url);
                if (params != null) {
                    mapping.setUrlParams(params);
                    return mapping;
                }
            }
        }
        
        return null;
    }

    public static List<String> getMappedUrlsFromContext(ServletContext context) {
        List<String> urls = (List<String>) context.getAttribute("framework.mappedUrls");
        return urls != null ? urls : new ArrayList<>();
    }
}