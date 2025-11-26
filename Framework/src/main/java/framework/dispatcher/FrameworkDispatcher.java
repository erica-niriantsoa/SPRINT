package framework.dispatcher;

import framework.ModelAndView.ModelAndView;
import framework.annotation.RequestParam;
import framework.scanner.AnnotationScanner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SPRINT 6 : Gère l'injection des paramètres et l'exécution des contrôleurs
 */
public class FrameworkDispatcher {
    
    public static Object processRequest(HttpServletRequest request, String path) 
    throws IOException {
        
        AnnotationScanner.MappingInfo mapping = AnnotationScanner.getMappingFromContext(
            request.getServletContext(), path);
            
        if (mapping != null) {
            return invokeControllerMethod(request, mapping);
        }
        
        return null;
    }
    
    private static Object invokeControllerMethod(HttpServletRequest request, 
                                               AnnotationScanner.MappingInfo mapping) {
        try {
            Object controllerInstance = mapping.getControllerClass().getDeclaredConstructor().newInstance();
            Method method = mapping.getMethod();
            
            Object[] methodArgs = prepareMethodArguments(request, method, mapping);
            return method.invoke(controllerInstance, methodArgs);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'execution: " + e.getMessage();
        }
    }
    
    private static Object[] prepareMethodArguments(HttpServletRequest request, Method method, 
                                                  AnnotationScanner.MappingInfo mapping) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        Map<String, String> urlParams = mapping.getUrlParams();
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();
            
            if (param.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = param.getAnnotation(RequestParam.class);
                String paramValue = request.getParameter(annotation.value());
                args[i] = convertParameter(paramValue, paramType);
            } else {
                String paramName = param.getName();
                String paramValue = null;
                
                if (urlParams != null && urlParams.containsKey(paramName)) {
                    paramValue = urlParams.get(paramName);
                } else {
                    paramValue = request.getParameter(paramName);
                }
                
                args[i] = convertParameter(paramValue, paramType);
            }
        }
        return args;
    }
    
    private static Object convertParameter(String value, Class<?> targetType) {
        if (value == null) return getDefaultValue(targetType);
        
        try {
            if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
            if (targetType == long.class || targetType == Long.class) return Long.parseLong(value);
            if (targetType == double.class || targetType == Double.class) return Double.parseDouble(value);
            if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);
            return value;
        } catch (NumberFormatException e) {
            return getDefaultValue(targetType);
        }
    }
    
    private static Object getDefaultValue(Class<?> type) {
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == double.class) return 0.0;
        if (type == boolean.class) return false;
        return null;
    }
    
    public static void displayMethodResult(PrintWriter out, String url, 
                                         AnnotationScanner.MappingInfo mapping, Object result) {
        StringBuilder info = new StringBuilder();
        info.append("=== METHODE EXECUTEE ===\n")
            .append("URL: ").append(url).append("\n")
            .append("Controleur: ").append(mapping.getControllerClass().getName()).append("\n")
            .append("Methode: ").append(mapping.getMethod().getName()).append("\n")
            .append("Type de retour: ").append(mapping.getMethod().getReturnType().getSimpleName()).append("\n");
        
        if (mapping.getUrlParams() != null && !mapping.getUrlParams().isEmpty()) {
            info.append("Parametres URL: ").append(mapping.getUrlParams()).append("\n");
        }
        
        info.append("Valeur retournee: ").append(result).append("\n")
            .append("Methode executee avec succes !");
        
        out.println(info.toString());
    }
    public static void displayWelcomePage(PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FRAMEWORK SPRINT 6 - URLs MAPPEES ===\n\n")
          .append("URLs detectees automatiquement:\n");
        
        // Récupérer les URLs depuis le contexte (à adapter selon ton implémentation)
        List<String> urls = Arrays.asList(
            "/etudiant/{id}",
            "/etudiant/{id}/notes/{matiere}", 
            "/recherche-etudiant",
            "/etudiant-details",
            "/etudiant/{id}/profil",
            "/cours/{id}/inscription"
        );
        
        for (String url : urls) {
            sb.append("  ").append(url).append("\n");
        }
        
        sb.append("\n=== Testez une URL dans la barre d'adresse ===");
        out.print(sb.toString());
    }
    
    public static void displayTextResult(HttpServletResponse response, String path, Object result) 
    throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("=== RESULTAT ===");
        out.println("URL: " + path);
        out.println("Resultat: " + result);
    }
    
    public static void displayNotFound(HttpServletResponse response, String url) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP 404 - Not Found\n")
          .append("=====================\n\n")
          .append("URL non trouvee: ").append(url).append("\n\n")
          .append("Consultez la page d'accueil pour plus de details: /");
        out.print(sb.toString());
    }
    
    public static void displayError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("ERREUR: " + message);
    }
}