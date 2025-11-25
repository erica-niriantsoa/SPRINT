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
import jakarta.servlet.ServletContext;

/**
 * Scanner qui initialise toutes les URLs au démarrage - SPRINT 3
 */
public class AnnotationScanner {
    
    private Map<String, MappingInfo> urlMappings = new HashMap<>();
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
        
        System.out.println(" " + urlMappings.size() + " URL(s) mappée(s)");
        displayMappedUrls();

        storeMappingsInContext();
    }
    
    private void storeMappingsInContext() {
        if (servletContext != null) {
            servletContext.setAttribute("framework.urlMappings", urlMappings);
            servletContext.setAttribute("framework.mappedUrls", new ArrayList<>(urlMappings.keySet()));
            System.out.println("  Mappings stockés dans ServletContext");
        }
    }
    
    private void mapControllerUrls(Class<?> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Url.class)) {
                Url urlAnnotation = method.getAnnotation(Url.class);
                String url = urlAnnotation.value();

                String cleanUrl = removeQueryParams(url);
                
                urlMappings.put(url, new MappingInfo(controllerClass, method));
                System.out.println(" " + url + " → " + 
                    controllerClass.getSimpleName() + "." + method.getName());
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
        return urlMappings.get(url);
    }
    
    public void displayMappedUrls() {
        System.out.println(" URLs supportées:");
        for (String url : urlMappings.keySet()) {
            MappingInfo mapping = urlMappings.get(url);
            System.out.println("  " + url + " → " + 
                mapping.getControllerClass().getSimpleName() + "." + mapping.getMethod().getName());
        }
    }
    
    public void displayMappedUrlsInHTML(PrintWriter out) {
        for (String url : urlMappings.keySet()) {
            MappingInfo mapping = urlMappings.get(url);
            out.println("<li><strong>" + url + "</strong> → " +
                mapping.getControllerClass().getSimpleName() + "." + mapping.getMethod().getName() + "</li>");
        }
    }
    
    public static class MappingInfo {
        private Class<?> controllerClass;
        private Method method;
        
        public MappingInfo(Class<?> controllerClass, Method method) {
            this.controllerClass = controllerClass;
            this.method = method;
        }
        
        public Class<?> getControllerClass() { return controllerClass; }
        public Method getMethod() { return method; }
    }

    public List<String> getMappedUrls() {
        return new ArrayList<>(urlMappings.keySet());
    }
    public static MappingInfo getMappingFromContext(ServletContext context, String url) {
        Map<String, MappingInfo> mappings = (Map<String, MappingInfo>) context.getAttribute("framework.urlMappings");
        return mappings != null ? mappings.get(url) : null;
    }

    public static List<String> getMappedUrlsFromContext(ServletContext context) {
        List<String> urls = (List<String>) context.getAttribute("framework.mappedUrls");
        return urls != null ? urls : new ArrayList<>();
    }

    
}