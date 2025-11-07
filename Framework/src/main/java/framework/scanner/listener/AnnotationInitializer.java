package framework.scanner.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import framework.scanner.AnnotationScanner;
import framework.scanner.AnnotationScanner.MappingInfo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AnnotationInitializer implements ServletContextListener {
    
    private static final String SCANNER_ATTRIBUTE = "framework.annotationScanner";
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println(" Initialisation du Framework...");
        
        
        AnnotationScanner scanner = new AnnotationScanner(event.getServletContext());
        scanner.initialize();
        
        event.getServletContext().setAttribute(SCANNER_ATTRIBUTE, scanner);
        
        System.out.println(" Framework initialisé - " + scanner.getMappedUrls().size() + " URLs mappées");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("🔚 Nettoyage du Framework");
        event.getServletContext().removeAttribute(SCANNER_ATTRIBUTE);
    }
    

    public static AnnotationScanner getScanner(ServletContextEvent event) {
        return (AnnotationScanner) event.getServletContext().getAttribute(SCANNER_ATTRIBUTE);
    }

    
}