package framework;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/*")
public class FrontServlet extends HttpServlet {

    RequestDispatcher defaultDispatcher;

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
            showPathPage(response, "/");
            return;
        }

        boolean resourceExists = getServletContext().getResource(path) != null;

        if (resourceExists) {
            defaultDispatcher.forward(request, response);
        } else {
            showPathPage(response, path);
        }
    }

    private void showPathPage(HttpServletResponse response, String path) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Path Display</title></head><body>");
            out.println("<h1>Chemin demandé :</h1>");
            out.println("<p><strong>" + path + "</strong></p>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}