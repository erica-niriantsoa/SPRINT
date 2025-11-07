package framework.servlet;

import framework.scanner.AnnotationScanner;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet("/*")
public class FrontServlet extends HttpServlet {

    RequestDispatcher defaultDispatcher;
    // private AnnotationScanner annotationScanner;
    // private AnnotationScanner annotationScanner;

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();        
        String context = request.getContextPath();   
        String path = uri.substring(context.length());

        if (path.equals("/") || path.equals("")) {
            response.setContentType("text/plain");
            displayAllMappings(response.getWriter());
            return;
        }

        boolean resourceExists = getServletContext().getResource(path) != null;
        if (resourceExists) {
            defaultDispatcher.forward(request, response);
            return;
        }

        processUrlMapping(request, response, path);
    }

    private void processUrlMapping(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        AnnotationScanner.MappingInfo mapping = AnnotationScanner.getMappingFromContext(getServletContext(), path);

        if(mapping != null){
            displayMappingInfo(out, path, mapping);
        } else {
            displayError(response,out, path);
        }
    }

    private void displayAllMappings(PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FRAMEWORK SPRINT 3 - URLs MAPPÉES ===\n\n")
        .append("URLs détectées automatiquement:\n");
        
        
        for (String url : AnnotationScanner.getMappedUrlsFromContext(getServletContext())) {
            sb.append("  ").append(url).append("\n");
        }
        
        sb.append("\n=== Testez une URL dans la barre d'adresse ===");
        out.print(sb.toString());
    }

    private void displayError(HttpServletResponse response, PrintWriter out, String url) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
    StringBuilder sb = new StringBuilder();
    sb.append("HTTP 404 - Not Found\n")
      .append("=====================\n\n");

    sb.append("\nConsultez la page d'accueil pour plus de détails: /");
    out.print(sb.toString());
}

    private void displayMappingInfo(PrintWriter out, String url, AnnotationScanner.MappingInfo mapping) {
        Class<?> controllerClass = mapping.getControllerClass();
        Method method = mapping.getMethod();
        
        out.println("=== URL MAPPÉE TROUVÉE ===" +
            "\nURL: " + url +
            "\nContrôleur: " + controllerClass.getName() +
            "\nMéthode: " + method.getName() +
            // "\nType: " + method.getReturnType().getSimpleName() +
            "\n Framework a reconnu cette URL !");
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}