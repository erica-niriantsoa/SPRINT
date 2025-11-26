// package test.controller;

// import framework.annotation.Url;
// import framework.ModelAndView.ModelAndView;
// import framework.annotation.Controller;

// @Controller
// public class Test1Controller {
    
//     @Url("/test-hello")
//     public String hello() {
//         return "/test1/hello.jsp";
//     }

//     @Url("/nombre")
//     public Double nombre() {
//         return 5.2;
//     }

//     @Url("/ModelAndView")
//     public ModelAndView mv() {
//         return new ModelAndView("/test.jsp");
//     }
    
//     @Url("/test-mv")
//     public ModelAndView testModelView() {
//         return new ModelAndView("/huh.jsp");
//     }
// }