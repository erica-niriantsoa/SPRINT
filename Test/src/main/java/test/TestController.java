package test;

import framework.Url;

public class TestController {
    
    @Url("/bonjour")
    public String direBonjour() {
        System.out.println("Methode direBonjour() appelee !");
        return "/bonjour.jsp";
    }
    
    @Url("/test-annotation")
    public String testAnnotation() {
        System.out.println("Methode testAnnotation() appelee !");
        return "/test-annotation.jsp";
    }
}