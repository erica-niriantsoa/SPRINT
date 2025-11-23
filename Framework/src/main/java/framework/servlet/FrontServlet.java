package framework.servlet;

import framework.ModelAndView.ModelAndView;
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
import java.util.Map;


public class FrontServlet extends HttpServlet {

    RequestDispatcher defaultDispatcher;
    // private AnnotationScanner annotationScanner;
    // private AnnotationScanner annotationScanner;

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

    // SPRINT4-bis
    private void handleModelView(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) 
throws IOException {
    
    String viewName = mv.getView();
    if (getServletContext().getResource(viewName) == null) {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(" ERREUR: La vue '" + viewName + "' n'existe pas");
        return;
    }

    Map<String, Object> model = mv.getModel();
    if (model != null) {
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }
    
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewName);
    try {
        dispatcher.forward(request, response);
    }   
    catch (Exception e) {
        // CORRECTION : Afficher l'erreur
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(" ERREUR lors du forward: " + e.getMessage());
        e.printStackTrace(); // Pour voir dans les logs
    }
}

    private void processUrlMapping(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
    AnnotationScanner.MappingInfo mapping = AnnotationScanner.getMappingFromContext(getServletContext(), path);

    if(mapping != null){
        Object result = invokeControllerMethod(mapping);

        if (result instanceof ModelAndView) {
            handleModelView(request, response, (ModelAndView) result);
        } else {
            // Seulement ici on prépare l'affichage texte
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            displayMethodResult(out, path, mapping, result);
        }
        
    } else {
        // Et ici pour les erreurs 404
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        displayError(response, out, path);
    }
}
    

    // SPRINT 4
    private Object invokeControllerMethod(AnnotationScanner.MappingInfo mapping){
        try {
        // Créer une instance du contrôleur
        Object controllerInstance = mapping.getControllerClass().getDeclaredConstructor().newInstance();
        
        // Invoquer la méthode
        Method method = mapping.getMethod();
        return method.invoke(controllerInstance);
        
    } catch (Exception e) {
        return " Erreur lors de l'exécution: " + e.getMessage();
    }
    }


    private void displayMethodResult(PrintWriter out, String url, AnnotationScanner.MappingInfo mapping, Object result) {
        Class<?> controllerClass = mapping.getControllerClass();
        Method method = mapping.getMethod();
        
        out.println("=== MÉTHODE EXÉCUTÉE ===" +
            "\nURL: " + url +
            "\nContrôleur: " + controllerClass.getName() +
            "\nMéthode: " + method.getName() +
            "\nType de retour: " + method.getReturnType().getSimpleName() +
            "\nValeur retournée: " + result +
            "\n Méthode exécutée avec succès !");
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


    
    
}