package framework.servlet;

import framework.ModelAndView.ModelAndView;
import framework.dispatcher.FrameworkDispatcher;
import framework.scanner.AnnotationScanner;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * FrontServlet allégé - Délègue la logique métier
 */
public class FrontServlet extends HttpServlet {

    RequestDispatcher defaultDispatcher;
    private static final String FRAMEWORK_PROCESSED = "framework.processed";

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();        
        String context = request.getContextPath();   
        String path = uri.substring(context.length());
        String cleanPath = removeQueryParams(path);        

        // Gestion des cas spéciaux
        if (isSpecialCase(request, response, cleanPath)) {
            return;
        }

        // Traitement framework
        processFrameworkRequest(request, response, cleanPath);
    }

    private boolean isSpecialCase(HttpServletRequest request, HttpServletResponse response, String path) 
    throws IOException {
        
        if (path.equals("/") || path.equals("")) {
            response.setContentType("text/plain");
            displayAllMappings(response.getWriter());
            return true;
        }

        if (request.getAttribute(FRAMEWORK_PROCESSED) != null) {
            try {
                getServletContext().getNamedDispatcher("jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (shouldExcludeFromFramework(path)) {
            try {
                defaultDispatcher.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return true;
        }

        boolean resourceExists = getServletContext().getResource(path) != null;
        if (resourceExists) {
            try {
                defaultDispatcher.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return true;
        }
        
        return false;
    }

    private void processFrameworkRequest(HttpServletRequest request, HttpServletResponse response, String path) 
    throws IOException {
        
        Object result = FrameworkDispatcher.processRequest(request, path);
        
        if (result != null) {
            if (result instanceof ModelAndView) {
                handleModelView(request, response, (ModelAndView) result);
            } else {
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                AnnotationScanner.MappingInfo mapping = AnnotationScanner.getMappingFromContext(
                    getServletContext(), path);
                FrameworkDispatcher.displayMethodResult(out, path, mapping, result);
            }
        } else {
            displayNotFound(response, path);
        }
    }

    private void handleModelView(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) 
    throws IOException {
        
        String viewName = mv.getView();
        
        if (getServletContext().getResource(viewName) == null) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("ERREUR: La vue '" + viewName + "' n'existe pas");
            return;
        }

        Map<String, Object> model = mv.getModel();
        if (model != null) {
            for (Map.Entry<String, Object> entry : model.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        
        request.setAttribute(FRAMEWORK_PROCESSED, true);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewName);
        try {
            dispatcher.forward(request, response);
        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("ERREUR lors du forward: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String removeQueryParams(String path) {
        if (path.contains("?")) {
            return path.substring(0, path.indexOf("?"));
        }
        return path;
    }
    
    private boolean shouldExcludeFromFramework(String path) {
        return path.endsWith(".css") || path.endsWith(".js") || 
               path.startsWith("/WEB-INF/") || path.startsWith("/images/") ||
               path.startsWith("/css/") || path.startsWith("/js/");
    }

    private void displayAllMappings(PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FRAMEWORK SPRINT 6-bis - URLs MAPPEES ===\n\n")
          .append("URLs detectees automatiquement:\n");
        
        for (String url : AnnotationScanner.getMappedUrlsFromContext(getServletContext())) {
            sb.append("  ").append(url).append("\n");
        }
        
        sb.append("\n=== Testez une URL dans la barre d'adresse ===");
        out.print(sb.toString());
    }

    private void displayNotFound(HttpServletResponse response, String url) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP 404 - Not Found\n")
          .append("=====================\n\n")
          .append("URL non trouvee: ").append(url).append("\n\n")
          .append("Consultez la page d'accueil pour plus de details: /");
        out.print(sb.toString());
    }
}