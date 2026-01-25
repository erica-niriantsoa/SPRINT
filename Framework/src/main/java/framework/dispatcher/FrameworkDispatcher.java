package framework.dispatcher;

import framework.annotation.AuthenticatedOnly;
import framework.annotation.FileParam;
import framework.annotation.RequestParam;
import framework.annotation.RestAPI;
import framework.annotation.RoleRequired;
import framework.annotation.Session;
import framework.mapping.MappingInfo;
import framework.response.ApiResponse;
import framework.scanner.AnnotationScanner;
import framework.security.AuthenticationManager;
import framework.security.User;
import framework.upload.FileUpload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * SPRINT 6 + SPRINT 7 + SPRINT 9 + SPRINT 11 + SPRINT 11-bis : Gere l'injection des parametres et l'execution des controleurs
 */
public class FrameworkDispatcher {
    
    /**
     * SPRINT 7 : Traite une requete avec httpMethod
     */
    public static Object processRequest(HttpServletRequest request, String path, String httpMethod) 
    throws IOException {
        
        // SPRINT 7 : Passer httpMethod à getMappingFromContext
        MappingInfo mapping = AnnotationScanner.getMappingFromContext(
            request.getServletContext(), path, httpMethod);
            
        if (mapping != null) {
            return invokeControllerMethod(request, mapping);
        }
        
        return null;
    }
    
    private static Object invokeControllerMethod(HttpServletRequest request, MappingInfo mapping) {
        try {
            Object controllerInstance = mapping.getControllerClass().getDeclaredConstructor().newInstance();
            Method method = mapping.getMethod();
            
            // SPRINT 11-bis : Vérification de la sécurité avant l'exécution
            String securityError = checkSecurity(request, method);
            if (securityError != null) {
                return securityError;
            }
            
            Object[] methodArgs = prepareMethodArguments(request, method, mapping);
            Object result = method.invoke(controllerInstance, methodArgs);
            
            // SPRINT 11 : Synchroniser la session après l'exécution de la méthode
            syncSessionBack(request, methodArgs, method.getParameters());
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'execution: " + e.getMessage();
        }
    }
    
    /**
     * SPRINT 11-bis : Vérifie les autorisations de sécurité pour une méthode
     * @param request La requête HTTP
     * @param method La méthode à exécuter
     * @return Un message d'erreur si accès refusé, null si autorisé
     */
    private static String checkSecurity(HttpServletRequest request, Method method) {
        HttpSession session = request.getSession(false);
        
        // Vérifier si la méthode nécessite une authentification
        boolean requiresAuth = method.isAnnotationPresent(AuthenticatedOnly.class);
        boolean requiresRole = method.isAnnotationPresent(RoleRequired.class);
        
        if (!requiresAuth && !requiresRole) {
            // Pas de restriction de sécurité
            return null;
        }
        
        // Récupérer l'utilisateur connecté depuis la session
        User currentUser = null;
        if (session != null) {
            currentUser = (User) session.getAttribute(AuthenticationManager.SESSION_USER_KEY);
        }
        
        // Vérifier l'authentification
        if (requiresAuth && currentUser == null) {
            return "ERREUR 401 : Accès refusé. Vous devez être authentifié pour accéder à cette ressource.";
        }
        
        // Vérifier le rôle si nécessaire
        if (requiresRole) {
            if (currentUser == null) {
                return "ERREUR 401 : Accès refusé. Vous devez être authentifié pour accéder à cette ressource.";
            }
            
            RoleRequired roleAnnotation = method.getAnnotation(RoleRequired.class);
            String[] requiredRoles = roleAnnotation.value();
            
            if (!AuthenticationManager.hasRole(currentUser, requiredRoles)) {
                return "ERREUR 403 : Accès interdit. Votre rôle (" + currentUser.getRole() + 
                       ") n'est pas autorisé à accéder à cette ressource.";
            }
        }
        
        return null; // Autorisé
    }
    
    /**
     * SPRINT 11 : Synchronise les modifications de la Map de session vers HttpSession
     */
    private static void syncSessionBack(HttpServletRequest request, Object[] methodArgs, Parameter[] parameters) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) return;
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (param.isAnnotationPresent(Session.class) && methodArgs[i] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> sessionMap = (Map<String, Object>) methodArgs[i];
                
                // Synchroniser les modifications vers HttpSession
                for (Map.Entry<String, Object> entry : sessionMap.entrySet()) {
                    httpSession.setAttribute(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    
    /**
     * SPRINT 6 + SPRINT 6-bis + SPRINT 6-ter + SPRINT 8-BIS + SPRINT 10 + SPRINT 11 : Injection de parametres
     */
    private static Object[] prepareMethodArguments(HttpServletRequest request, Method method, 
                                                  MappingInfo mapping) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        // SPRINT 6 : Recuperer variables URL
        Map<String, String> urlParams = mapping.getUrlParams();
        
        // SPRINT 10 : Récupérer les fichiers uploadés
        Map<String, FileUpload> uploadedFiles = extractUploadedFiles(request);
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();
            
            // SPRINT 11 : @Session - Injection de la session HTTP comme Map
            if (param.isAnnotationPresent(Session.class)) {
                if (Map.class.isAssignableFrom(paramType)) {
                    args[i] = extractSessionAsMap(request);
                } else {
                    args[i] = null; // Type non supporté pour @Session
                }
                continue;
            }
            
            // SPRINT 11 : Injection automatique de HttpServletRequest
            if (paramType == HttpServletRequest.class) {
                args[i] = request;
                continue;
            }
            
            // SPRINT 10 : @FileParam - Fichier uploadé
            if (param.isAnnotationPresent(FileParam.class)) {
                FileParam annotation = param.getAnnotation(FileParam.class);
                String fileName = annotation.value();
                FileUpload fileUpload = uploadedFiles.get(fileName);
                
                // Si le paramètre attend byte[], on donne seulement le contenu
                if (paramType == byte[].class) {
                    args[i] = fileUpload != null ? fileUpload.getContent() : null;
                } else {
                    // Sinon on donne l'objet FileUpload complet
                    args[i] = fileUpload;
                }
                continue;
            }
            
            // SPRINT 8-BIS : Si c'est un objet personnalisé (pas primitif, pas String, pas Map)
            if (isCustomObject(paramType)) {
                args[i] = buildObjectFromParams(request, paramType, mapping);
                continue;
            }
            
            // SPRINT 6-ter : @RequestParam
            if (param.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = param.getAnnotation(RequestParam.class);
                String paramValue = request.getParameter(annotation.value());
                args[i] = convertParameter(paramValue, paramType);
            } 
            // SPRINT 6 + SPRINT 6-bis : Convention de noms
            else {
                String paramName = param.getName();
                String paramValue = null;
                
                // SPRINT 6 : Variables URL
                if (urlParams != null && urlParams.containsKey(paramName)) {
                    paramValue = urlParams.get(paramName);
                } 
                // SPRINT 6-bis : Query params
                else {
                    paramValue = request.getParameter(paramName);
                }
                
                args[i] = convertParameter(paramValue, paramType);
            }
        }
        return args;
    }
    
    /**
     * SPRINT 11 : Extrait la HttpSession comme Map<String, Object>
     * Crée une copie de tous les attributs de la session
     */
    private static Map<String, Object> extractSessionAsMap(HttpServletRequest request) {
        Map<String, Object> sessionMap = new HashMap<>();
        HttpSession httpSession = request.getSession(true); // Créer si n'existe pas
        
        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            Object value = httpSession.getAttribute(key);
            sessionMap.put(key, value);
        }
        
        return sessionMap;
    }
    
    /**
     * SPRINT 10 : Extrait tous les fichiers uploadés de la requête
     * @return Map avec le nom du champ comme clé et l'objet FileUpload comme valeur
     */
    private static Map<String, FileUpload> extractUploadedFiles(HttpServletRequest request) {
        Map<String, FileUpload> files = new HashMap<>();
        
        try {
            // Vérifier si la requête contient des fichiers (multipart/form-data)
            if (request.getContentType() != null && 
                request.getContentType().toLowerCase().startsWith("multipart/form-data")) {
                
                Collection<Part> parts = request.getParts();
                
                for (Part part : parts) {
                    // Vérifier si c'est un fichier (et pas un champ texte)
                    if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                        String fieldName = part.getName();
                        String fileName = part.getSubmittedFileName();
                        String contentType = part.getContentType();
                        
                        // Lire le contenu du fichier en byte array
                        try (InputStream inputStream = part.getInputStream()) {
                            byte[] fileContent = inputStream.readAllBytes();
                            FileUpload fileUpload = new FileUpload(fileName, contentType, fileContent);
                            files.put(fieldName, fileUpload);
                        }
                    }
                }
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        
        return files;
    }
    
    /**
     * SPRINT 8-BIS : Vérifie si le type est un objet personnalisé
     * (ni primitif, ni String, ni Map, ni Array, ni type Java commun)
     */
    private static boolean isCustomObject(Class<?> type) {
        if (type.isArray()) return false;
        
        if (type.isPrimitive()) return false;
        if (type == Integer.class || type == Long.class || type == Double.class || 
            type == Boolean.class || type == Float.class || type == Short.class ||
            type == Byte.class || type == Character.class) return false;
        
        if (type == String.class) return false;
        if (Map.class.isAssignableFrom(type)) return false;
        
        if (type.getName().startsWith("java.")) return false;
        if (type.getName().startsWith("jakarta.")) return false;
        
        return true;
    }
    
    /**
     * SPRINT 8-BIS : Construit un objet à partir des paramètres HTTP
     * Utilise la reflection pour :
     * 1. Créer une instance de l'objet
     * 2. Pour chaque champ, récupérer la valeur du paramètre HTTP correspondant
     * 3. Convertir et setter la valeur via le setter approprié
     */
    private static Object buildObjectFromParams(HttpServletRequest request, Class<?> objectType,
                                               MappingInfo mapping) {
        try {
            Object instance = objectType.getDeclaredConstructor().newInstance();
            
            java.lang.reflect.Field[] fields = objectType.getDeclaredFields();
            
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                String paramName = field.getName();
                String paramValue = request.getParameter(paramName);

                if (paramValue != null) {
                    Class<?> fieldType = field.getType();

                    if (fieldType.isArray()) {
                        Class<?> componentType = fieldType.getComponentType();
                        String[] values = paramValue.split(",");
                        Object array = java.lang.reflect.Array.newInstance(componentType, values.length);

                        for (int i = 0; i < values.length; i++) {
                            java.lang.reflect.Array.set(array, i, convertParameter(values[i].trim(), componentType));
                        }
                        field.set(instance, array);
                    } else {
                        field.set(instance, convertParameter(paramValue, fieldType));
                    }
                }
            }
            
            return instance;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Object convertParameter(String value, Class<?> targetType) {
        if (value == null) return getDefaultValue(targetType);

        try {
            if (targetType.isArray()) {
                Class<?> componentType = targetType.getComponentType();
                String[] values = value.split(","); // Suppose que les valeurs sont séparées par des virgules
                Object array = java.lang.reflect.Array.newInstance(componentType, values.length);

                for (int i = 0; i < values.length; i++) {
                    java.lang.reflect.Array.set(array, i, convertParameter(values[i].trim(), componentType));
                }
                return array;
            }

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
    
    /**
     * SPRINT 7 : Affiche le resultat avec httpMethod
     */
    public static void displayMethodResult(PrintWriter out, String url, MappingInfo mapping, 
                                          Object result, String httpMethod) {
        StringBuilder info = new StringBuilder();
        info.append("=== METHODE EXECUTEE ===\n")
            .append("URL: ").append(url).append("\n")
            .append("HTTP Method: ").append(httpMethod).append("\n")
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
    
    public static void displayTextResult(HttpServletResponse response, String path, Object result) 
    throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("=== RESULTAT ===");
        out.println("URL: " + path);
        out.println("Resultat: " + result);
    }
    
    public static void displayNotFound(HttpServletResponse response, String url, String httpMethod) 
    throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP 404 - Not Found\n")
          .append("=====================\n\n")
          .append("URL: ").append(url).append("\n")
          .append("HTTP Method: ").append(httpMethod).append("\n\n")
          .append("Aucun mapping trouve\n\n")
          .append("Consultez la page d'accueil pour plus de details: /");
        out.print(sb.toString());
    }
    
    public static void displayError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("ERREUR: " + message);
    }
    
    // ========================================================================
    // SPRINT 9 : Gestion des réponses JSON pour API REST
    // ========================================================================
    
    /**
     * SPRINT 9 : Vérifie si la méthode doit retourner du JSON
     */
    public static boolean isRestAPI(Method method) {
        return method.isAnnotationPresent(RestAPI.class);
    }
    
    /**
     * SPRINT 9 : Convertit un objet en JSON et envoie la réponse
     */
    public static void sendJsonResponse(HttpServletResponse response, Object result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        ApiResponse apiResponse;
        
        // Si le résultat est déjà une ApiResponse, l'utiliser directement
        if (result instanceof ApiResponse) {
            apiResponse = (ApiResponse) result;
        }
        // Si c'est un ModelAndView, extraire les données
        else if (result != null && result.getClass().getName().contains("ModelAndView")) {
            try {
                // Utiliser reflection pour récupérer les données du ModelAndView
                Field dataField = result.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                Object data = dataField.get(result);
                apiResponse = ApiResponse.success(data);
            } catch (Exception e) {
                apiResponse = ApiResponse.error(500, "Erreur lors de l'extraction des données");
            }
        }
        // Sinon, wrapper l'objet dans une ApiResponse
        else {
            apiResponse = ApiResponse.success(result);
        }
        
        // Convertir en JSON
        String json = convertToJson(apiResponse);
        out.print(json);
    }
    
    /**
     * SPRINT 9 : Convertit un objet en JSON (conversion manuelle simple)
     */
    private static String convertToJson(Object obj) {
        if (obj == null) {
            return "null";
        }
    
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        
        if (obj instanceof Collection || obj.getClass().isArray()) {
            return convertCollectionToJson(obj);
        }
        
        if (obj instanceof Map) {
            return convertMapToJson((Map<?, ?>) obj);
        }
        
        return convertObjectToJson(obj);
    }
    
    /**
     * Convertit un objet en JSON via reflection
     */
    private static String convertObjectToJson(Object obj) {
        StringBuilder json = new StringBuilder("{");
        
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            boolean first = true;
            
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                
                if (!first) json.append(",");
                first = false;
                
                json.append("\"").append(field.getName()).append("\":");
                json.append(convertToJson(value));
            }
        } catch (Exception e) {
            return "{}";
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Convertit une Map en JSON
     */
    private static String convertMapToJson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) json.append(",");
            first = false;
            
            json.append("\"").append(entry.getKey().toString()).append("\":");
            json.append(convertToJson(entry.getValue()));
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Convertit une Collection ou Array en JSON
     */
    private static String convertCollectionToJson(Object obj) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        if (obj instanceof Collection) {
            for (Object item : (Collection<?>) obj) {
                if (!first) json.append(",");
                first = false;
                json.append(convertToJson(item));
            }
        } else if (obj.getClass().isArray()) {
            Object[] array = (Object[]) obj;
            for (Object item : array) {
                if (!first) json.append(",");
                first = false;
                json.append(convertToJson(item));
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Échappe les caractères spéciaux pour JSON
     */
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}