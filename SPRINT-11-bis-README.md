# Sprint 11-bis : Système d'authentification et d'autorisation

## Implémentation complète

### 1. Annotations de sécurité créées

#### @AuthenticatedOnly
- Localisation : [Framework/src/main/java/framework/annotation/AuthenticatedOnly.java](Framework/src/main/java/framework/annotation/AuthenticatedOnly.java)
- Usage : Annoter les méthodes qui nécessitent une authentification
- Exemple : `@AuthenticatedOnly`

#### @RoleRequired
- Localisation : [Framework/src/main/java/framework/annotation/RoleRequired.java](Framework/src/main/java/framework/annotation/RoleRequired.java)
- Usage : Spécifier le(s) rôle(s) requis pour accéder à une méthode
- Exemple : `@RoleRequired({"chef", "admin"})`

### 2. Système d'authentification avec données en dur

#### Classe User
- Localisation : [Framework/src/main/java/framework/security/User.java](Framework/src/main/java/framework/security/User.java)
- Représente un utilisateur avec username, password et role

#### AuthenticationManager
- Localisation : [Framework/src/main/java/framework/security/AuthenticationManager.java](Framework/src/main/java/framework/security/AuthenticationManager.java)
- Gère l'authentification avec une Map statique contenant les utilisateurs
- Utilisateurs prédéfinis :
  - **admin** / admin123 (rôle: admin)
  - **chef** / chef123 (rôle: chef)
  - **employe** / employe123 (rôle: employe)
  - **user** / user123 (rôle: user)

### 3. Modifications du FrameworkDispatcher

- Localisation : [Framework/src/main/java/framework/dispatcher/FrameworkDispatcher.java](Framework/src/main/java/framework/dispatcher/FrameworkDispatcher.java)
- Ajout de la méthode `checkSecurity()` qui :
  - Vérifie la présence des annotations @AuthenticatedOnly et @RoleRequired
  - Récupère l'utilisateur connecté depuis la session HTTP
  - Retourne une erreur 401 si non authentifié
  - Retourne une erreur 403 si le rôle n'est pas autorisé
- La vérification est effectuée **avant** chaque appel de méthode de contrôleur

### 4. Contrôleur de test SecurityController

- Localisation : [Test/src/main/java/test/controller/SecurityController.java](Test/src/main/java/test/controller/SecurityController.java)
- Routes implémentées :
  - `GET /login` : Formulaire de connexion
  - `POST /login` : Authentification et stockage en session
  - `GET /logout` : Déconnexion (invalidation de session)
  - `GET /protected` : Page protégée (@AuthenticatedOnly)
  - `GET /chef-only` : Page réservée aux chefs (@RoleRequired({"chef"}))
  - `GET /admin-area` : Zone admin (@RoleRequired({"chef", "admin"}))
  - `GET /public` : Page publique (sans restriction)
  - `GET /api/current-user` : API REST (@RestAPI + @AuthenticatedOnly)

### 5. Pages JSP de test

Toutes les pages sont dans : `Test/src/main/webapp/views/Sprint-11-bis/`

- [login-form.jsp](Test/src/main/webapp/views/Sprint-11-bis/login-form.jsp) : Formulaire de connexion avec liste des comptes disponibles
- [login-success.jsp](Test/src/main/webapp/views/Sprint-11-bis/login-success.jsp) : Confirmation de connexion
- [protected-page.jsp](Test/src/main/webapp/views/Sprint-11-bis/protected-page.jsp) : Page nécessitant une authentification
- [chef-page.jsp](Test/src/main/webapp/views/Sprint-11-bis/chef-page.jsp) : Page réservée au rôle "chef"
- [admin-page.jsp](Test/src/main/webapp/views/Sprint-11-bis/admin-page.jsp) : Page pour "chef" et "admin"
- [public-page.jsp](Test/src/main/webapp/views/Sprint-11-bis/public-page.jsp) : Page accessible à tous
- [logout-success.jsp](Test/src/main/webapp/views/Sprint-11-bis/logout-success.jsp) : Confirmation de déconnexion

## Fonctionnement

1. **Stockage en session** : L'utilisateur authentifié est stocké dans la session HTTP avec la clé `framework_authenticated_user`

2. **Vérification automatique** : Avant chaque exécution de méthode de contrôleur, FrameworkDispatcher vérifie :
   - Si la méthode a @AuthenticatedOnly → l'utilisateur doit être connecté
   - Si la méthode a @RoleRequired → l'utilisateur doit avoir un des rôles spécifiés

3. **Messages d'erreur** :
   - Erreur 401 : Utilisateur non authentifié
   - Erreur 403 : Utilisateur authentifié mais rôle insuffisant

## Test de l'implémentation

1. Compilez et déployez le projet
2. Accédez à : `http://localhost:8080/Test/login`
3. Testez différents comptes avec différents rôles
4. Essayez d'accéder aux pages protégées sans être connecté
5. Connectez-vous avec un compte "employe" et essayez d'accéder à `/chef-only`

## Exemple d'utilisation dans un contrôleur

```java
@Controller
public class MonController {
    
    // Page publique (pas d'annotation)
    @Get
    @Url("/public")
    public String pagePublique() {
        return "page.jsp";
    }
    
    // Nécessite authentification uniquement
    @Get
    @Url("/profile")
    @AuthenticatedOnly
    public String monProfil(@Session Map<String, Object> session) {
        User user = (User) session.get(AuthenticationManager.SESSION_USER_KEY);
        return "profil.jsp";
    }
    
    // Nécessite le rôle "chef"
    @Get
    @Url("/manage")
    @RoleRequired({"chef"})
    public String gestion() {
        return "gestion.jsp";
    }
    
    // Nécessite le rôle "chef" OU "admin"
    @Post
    @Url("/delete")
    @RoleRequired({"chef", "admin"})
    public String supprimer(@RequestParam("id") String id) {
        // Logique de suppression
        return "success.jsp";
    }
}
```
