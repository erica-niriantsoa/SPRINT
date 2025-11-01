package test;

import framework.annotation.Url;
import framework.annotation.Controller;
import framework.annotation.scanner.ClassPathScanner;
import java.lang.reflect.Method;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TEST COMPLET ANNOTATIONS ===");
        System.out.println("SPRINT 2: Annotations @Url");
        System.out.println("SPRINT 2-bis: Scanner automatique");
        
        // ==================== SPRINT 2 ====================
        System.out.println("\n--- SPRINT 2: ANNOTATIONS @Url ---");
        testUrlAnnotations();
        
        // ==================== SPRINT 2-bis ====================
        System.out.println("\n--- SPRINT 2-bis: SCAN AUTOMATIQUE ---");
        testAutomaticScanner();
    }
    
    private static void testUrlAnnotations() {
        try {
            Class<?> controllerClass = Class.forName("test.TestController");
            
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Url.class)) {
                    Url urlAnnotation = method.getAnnotation(Url.class);
                    String url = urlAnnotation.value();
                    
                    System.out.println("URL: " + url + " -> " + method.getName());
                    
                    Object controller = controllerClass.newInstance();
                    String result = (String) method.invoke(controller);
                    System.out.println("   Retourne: " + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testAutomaticScanner() {
        ClassPathScanner scanner = new ClassPathScanner();
        
        // Scanner le package test.controller
        List<Class<?>> controllers = scanner.findAnnotatedClasses("test.controller", Controller.class);
        
        System.out.println(controllers.size() + " controller(s) trouve(s) automatiquement");
        
        for (Class<?> controllerClass : controllers) {
            System.out.println("\nController: " + controllerClass.getSimpleName());
            
            // Afficher les méthodes @Url de chaque controller
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Url.class)) {
                    Url urlAnnotation = method.getAnnotation(Url.class);
                    String url = urlAnnotation.value();
                    System.out.println("   URL: " + url + " -> " + method.getName());
                    
                    // Tester l'exécution
                    try {
                        Object controller = controllerClass.newInstance();
                        String result = (String) method.invoke(controller);
                        System.out.println("      Retourne: " + result);
                    } catch (Exception e) {
                        System.out.println("      Erreur: " + e.getMessage());
                    }
                }
            }
        }
    }
}