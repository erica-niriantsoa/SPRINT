package test.controller;

import framework.annotation.Url;
import framework.annotation.Controller;

@Controller
public class Test2Controller {
    
    @Url("/test2-bonjour")
    public String bonjour() {
        return "/test2/bonjour.jsp";
    }
}