package test.controller;

import framework.annotation.Url;

import java.util.Arrays;
import java.util.List;

import framework.ModelAndView.ModelAndView;
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

    @Url("/departements")
    public ModelAndView listDepartements() {
        ModelAndView mv = new ModelAndView("/views/departements.jsp");
        
        List<String> departements = Arrays.asList(
            "Informatique", "Mathématiques", "Physique", "Chimie", "Biologie"
        );
        
        mv.addObject("departements", departements);
        mv.addObject("titre", "Liste des départements universitaires");
        mv.addObject("nombreDepts", departements.size());
        
        return mv;
    }
}