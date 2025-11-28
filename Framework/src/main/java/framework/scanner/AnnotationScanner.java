package framework.scanner;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.Post;
import framework.annotation.Url;
import framework.annotation.scanner.ClassPathScanner;
import framework.mapping.MappingInfo;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletContext;

/**
 * SPRINT 7 : Scanner qui detecte les URLs avec distinction GET/POST
 */
public class AnnotationScanner {
    
    private Map<String, Map<String, MappingInfo>> urlMappings = new HashMap<>();
    private Map<String, List<MappingInfo>> patternMappings = new HashMap<>();
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
        
        int totalMappings = countTotalMappings();
        System.out.println(" " + totalMappings + " URL(s) mappee(s)");
        displayMappedUrls();

        storeMappingsInContext();
    }
    
    private int countTotalMappings() {
        int count = 0;
        for (Map<String, MappingInfo> methods : urlMappings.values()) {
            count += methods.size();
        }
        for (List<MappingInfo> patterns : patternMappings.values()) {
            count += patterns.size();
        }
        return count;
    }
    
    private void storeMappingsInContext() {
        if (servletContext != null) {
            servletContext.setAttribute("framework.urlMappings", urlMappings);
            servletContext.setAttribute("framework.patternMappings", patternMappings);
            System.out.println("  Mappings stockes dans ServletContext");
        }
    }
    
    private void mapControllerUrls(Class<?> controllerClass) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Url.class)) {
                Url urlAnnotation = method.getAnnotation(Url.class);
                String url = urlAnnotation.value();
                String cleanUrl = removeQueryParams(url);
                
                String httpMethod = determineHttpMethod(method);
                MappingInfo mapping = new MappingInfo(controllerClass, method, cleanUrl, httpMethod);
                
                if (cleanUrl.contains("{") && cleanUrl.contains("}")) {
                    storePatternMapping(httpMethod, mapping);
                    System.out.println(" " + cleanUrl + " [" + httpMethod + "] (pattern) -> " + 
                        controllerClass.getSimpleName() + "." + method.getName());
                } else {
                    storeUrlMapping(cleanUrl, httpMethod, mapping);
                    System.out.println(" " + cleanUrl + " [" + httpMethod + "] -> " + 
                        controllerClass.getSimpleName() + "." + method.getName());
                }
            }
        }
    }

    private String determineHttpMethod(Method method) {
        if (method.isAnnotationPresent(Get.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(Post.class)) {
            return "POST";
        }
        return "GET";
    }

    private void storeUrlMapping(String url, String httpMethod, MappingInfo mapping) {
        if (!urlMappings.containsKey(url)) {
            urlMappings.put(url, new HashMap<>());
        }
        urlMappings.get(url).put(httpMethod, mapping);
    }

    private void storePatternMapping(String httpMethod, MappingInfo mapping) {
        if (!patternMappings.containsKey(httpMethod)) {
            patternMappings.put(httpMethod, new ArrayList<>());
        }
        patternMappings.get(httpMethod).add(mapping);
    }

    private String removeQueryParams(String url) {
        if (url.contains("?")) {
            return url.substring(0, url.indexOf("?"));
        }
        return url;
    }
    
    public MappingInfo getMapping(String url, String httpMethod) {
        if (urlMappings.containsKey(url)) {
            Map<String, MappingInfo> methods = urlMappings.get(url);
            if (methods.containsKey(httpMethod)) {
                return methods.get(httpMethod);
            }
        }
        
        List<MappingInfo> patterns = patternMappings.get(httpMethod);
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
    
    public void displayMappedUrls() {
        System.out.println(" URLs supportees:");
        
        for (String url : urlMappings.keySet()) {
            Map<String, MappingInfo> methods = urlMappings.get(url);
            for (String httpMethod : methods.keySet()) {
                MappingInfo mapping = methods.get(httpMethod);
                System.out.println("  " + mapping.toString());
            }
        }
        
        for (String httpMethod : patternMappings.keySet()) {
            List<MappingInfo> patterns = patternMappings.get(httpMethod);
            for (MappingInfo mapping : patterns) {
                System.out.println("  " + mapping.toString());
            }
        }
    }
    
    public void displayMappedUrlsInHTML(PrintWriter out) {
        for (String url : urlMappings.keySet()) {
            Map<String, MappingInfo> methods = urlMappings.get(url);
            for (String httpMethod : methods.keySet()) {
                MappingInfo mapping = methods.get(httpMethod);
                out.println("<li>" + mapping.toString() + "</li>");
            }
        }
        
        for (String httpMethod : patternMappings.keySet()) {
            List<MappingInfo> patterns = patternMappings.get(httpMethod);
            for (MappingInfo mapping : patterns) {
                out.println("<li>" + mapping.toString() + "</li>");
            }
        }
    }
    
    public List<String> getMappedUrls() {
        List<String> allUrls = new ArrayList<>(urlMappings.keySet());
        for (List<MappingInfo> patterns : patternMappings.values()) {
            for (MappingInfo mapping : patterns) {
                if (!allUrls.contains(mapping.getUrlPattern())) {
                    allUrls.add(mapping.getUrlPattern());
                }
            }
        }
        return allUrls;
    }
    
    public static MappingInfo getMappingFromContext(ServletContext context, String url, String httpMethod) {
        Map<String, Map<String, MappingInfo>> mappings = 
            (Map<String, Map<String, MappingInfo>>) context.getAttribute("framework.urlMappings");
        
        if (mappings != null && mappings.containsKey(url)) {
            Map<String, MappingInfo> methods = mappings.get(url);
            if (methods.containsKey(httpMethod)) {
                return methods.get(httpMethod);
            }
        }
        
        Map<String, List<MappingInfo>> patterns = 
            (Map<String, List<MappingInfo>>) context.getAttribute("framework.patternMappings");
        
        if (patterns != null && patterns.containsKey(httpMethod)) {
            List<MappingInfo> patternList = patterns.get(httpMethod);
            for (MappingInfo mapping : patternList) {
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
        List<String> urls = new ArrayList<>();
        
        Map<String, Map<String, MappingInfo>> mappings = 
            (Map<String, Map<String, MappingInfo>>) context.getAttribute("framework.urlMappings");
        
        if (mappings != null) {
            urls.addAll(mappings.keySet());
        }
        
        return urls;
    }
}