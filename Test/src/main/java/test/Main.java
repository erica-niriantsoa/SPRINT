package test;

import framework.Url;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TEST ANNOTATIONS @Url ===");
        
        try {
            Class<?> controllerClass = Class.forName("test.TestController");
            
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Url.class)) {
                    Url urlAnnotation = method.getAnnotation(Url.class);
                    String url = urlAnnotation.value();
                    
                    System.out.println("URL: " + url + " -> Methode: " + method.getName());
                    
                    Object controller = controllerClass.newInstance();
                    String result = (String) method.invoke(controller);
                    System.out.println("Retourne: " + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}