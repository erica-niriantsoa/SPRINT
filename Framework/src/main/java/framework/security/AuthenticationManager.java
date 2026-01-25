package framework.security;

import java.util.HashMap;
import java.util.Map;

/**
 * SPRINT 11-bis : Classe pour gérer l'authentification avec des données en dur
 */
public class AuthenticationManager {
    
    // Base de données en dur des utilisateurs
    private static final Map<String, User> users = new HashMap<>();
    
    // Clé pour stocker l'utilisateur dans la session
    public static final String SESSION_USER_KEY = "framework_authenticated_user";
    
    static {
        // Initialisation des utilisateurs en dur
        users.put("admin", new User("admin", "admin123", "admin"));
        users.put("chef", new User("chef", "chef123", "chef"));
        users.put("employe", new User("employe", "employe123", "employe"));
        users.put("user", new User("user", "user123", "user"));
    }
    
    /**
     * Authentifie un utilisateur avec son nom et mot de passe
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return L'utilisateur si authentification réussie, null sinon
     */
    public static User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    /**
     * Vérifie si un utilisateur a un rôle spécifique
     * @param user Utilisateur à vérifier
     * @param requiredRoles Rôles autorisés
     * @return true si l'utilisateur a un des rôles requis
     */
    public static boolean hasRole(User user, String[] requiredRoles) {
        if (user == null || requiredRoles == null || requiredRoles.length == 0) {
            return false;
        }
        
        String userRole = user.getRole();
        for (String role : requiredRoles) {
            if (role.equalsIgnoreCase(userRole)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Ajoute un utilisateur à la base (pour tests)
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @param role Rôle de l'utilisateur
     */
    public static void addUser(String username, String password, String role) {
        users.put(username, new User(username, password, role));
    }
}
