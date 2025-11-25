package test;

import framework.annotation.Url;  // ← IMPORT CORRIGÉ
import framework.annotation.Controller;

@Controller
public class TestController {
    
    @Url("/bonjour")
    public String direBonjour() {
        System.out.println("✅ Méthode direBonjour() appelée !");
        return "/bonjour.jsp";
    }
    
    @Url("/test-annotation")
    public String testAnnotation() {
        System.out.println("✅ Méthode testAnnotation() appelée !");
        return "/test-annotation.jsp";
    }

  
    
    @Url("/hello")
    public String hello() {
        return "hello.jsp";
    }
    
    @Url("/test")
    public String test() {
        return "test.jsp";
    }

}