package test.controller;

import framework.annotation.Url;
import framework.annotation.Controller;

@Controller  
public class Test3Controller {
    
    @Url("/test3-method1")
    public String method1() {
        System.out.println("✅ Test3Controller.method1() appelé !");
        return "/test3/method1.jsp";
    }
    
    @Url("/test3-method2")
    public String method2() {
        System.out.println("✅ Test3Controller.method2() appelé !");
        return "/test3/method2.jsp";
    }
}