package test.controller;

import framework.annotation.*;
import framework.security.AuthenticationManager;
import framework.security.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

/**
 * SPRINT 11-bis : Contrôleur pour gérer l'authentification et l'autorisation
 */
@Controller
public class SecurityController {
    
    /**
     * Affiche le formulaire de connexion
     */
    @Get
    @Url("/login")
    public String loginForm() {
        return "views/Sprint-11-bis/login-form.jsp";
    }
    
    /**
     * Traite la connexion d'un utilisateur
     */
    @Post
    @Url("/login")
    public String login(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        HttpServletRequest request
    ) {
        // Authentifier l'utilisateur
        User user = AuthenticationManager.authenticate(username, password);
        
        if (user != null) {
            // Stocker l'utilisateur dans la session
            HttpSession session = request.getSession(true);
            session.setAttribute(AuthenticationManager.SESSION_USER_KEY, user);
            
            return "views/Sprint-11-bis/login-success.jsp";
        } else {
            request.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            return "views/Sprint-11-bis/login-form.jsp";
        }
    }
    
    /**
     * Déconnexion de l'utilisateur
     */
    @Get
    @Url("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "views/Sprint-11-bis/logout-success.jsp";
    }
    
    /**
     * Page accessible uniquement aux utilisateurs authentifiés
     */
    @Get
    @Url("/protected")
    @AuthenticatedOnly
    public String protectedPage(@Session Map<String, Object> session) {
        User user = (User) session.get(AuthenticationManager.SESSION_USER_KEY);
        return "views/Sprint-11-bis/protected-page.jsp";
    }
    
    /**
     * Page accessible uniquement aux chefs
     */
    @Get
    @Url("/chef-only")
    @RoleRequired({"chef"})
    public String chefOnlyPage(@Session Map<String, Object> session) {
        User user = (User) session.get(AuthenticationManager.SESSION_USER_KEY);
        return "views/Sprint-11-bis/chef-page.jsp";
    }
    
    /**
     * Page accessible aux chefs et admins
     */
    @Get
    @Url("/admin-area")
    @RoleRequired({"chef", "admin"})
    public String adminArea(@Session Map<String, Object> session) {
        User user = (User) session.get(AuthenticationManager.SESSION_USER_KEY);
        return "views/Sprint-11-bis/admin-page.jsp";
    }
    
    /**
     * Page publique (pas d'authentification requise)
     */
    @Get
    @Url("/public")
    public String publicPage() {
        return "views/Sprint-11-bis/public-page.jsp";
    }
    
    /**
     * API REST pour obtenir les informations de l'utilisateur connecté
     */
    @Get
    @Url("/api/current-user")
    @RestAPI
    @AuthenticatedOnly
    public User getCurrentUser(@Session Map<String, Object> session) {
        return (User) session.get(AuthenticationManager.SESSION_USER_KEY);
    }
}
