package test.controller;

import framework.annotation.Url;
import framework.annotation.Controller;

@Controller
public class Test1Controller {
    
    @Url("/test-hello")
    public String hello() {
        return "/test1/hello.jsp";
    }

    @Url("/tsyaikoe")
    public Double tsyaiko() {
        return 5.2;
    }
}