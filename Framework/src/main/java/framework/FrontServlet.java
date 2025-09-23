package framework;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/*") // Capture toutes les URLs
public class FrontServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();        
        String context = request.getContextPath();   
        String path = uri.substring(context.length());

        // Si l'utilisateur tape directement "/" ou "", on redirige vers index.jsp
        if (path.equals("/") || path.equals("")) {
            // Forward vers JSP, pas via le FrontServlet
            request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
            return;
        }

        // Construire le chemin du JSP correspondant dans /WEB-INF/jsp
        String jspPath = "/WEB-INF/jsp" + path + ".jsp";

        // Vérifier si le JSP existe
        if (getServletContext().getResource(jspPath) != null) {
            request.getRequestDispatcher(jspPath).forward(request, response);
            return;
        }

        // Si aucun JSP trouvé → afficher un message d'erreur directement
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Page non trouvée</title></head><body>");
            out.println("<h2>404 - Page non trouvée</h2>");
            out.println("<p>Vous avez tapé : " + path + "</p>");
            out.println("</body></html>");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Reuse doGet
    }
}
