package test.controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.Post;
import framework.annotation.RequestParam;
import framework.annotation.Url;
import java.util.Map;

@Controller
public class TestController {
    
    // ========================================================================
    // TEST 1 : Même URL, GET vs POST - Simple
    // ========================================================================
    
    @Url("/api/test")
    @Get
    public String testGet() {
        return "SPRINT 7 GET : Ceci est la reponse GET sur /api/test";
    }
    
    @Url("/api/test")
    @Post
    public String testPost() {
        return "SPRINT 7 POST : Ceci est la reponse POST sur /api/test";
    }
    
    // ========================================================================
    // TEST 2 : Même URL avec paramètres
    // ========================================================================
    
    @Url("/api/etudiant")
    @Get
    public String listerEtudiants() {
        return "SPRINT 7 GET : Liste de tous les etudiants [Jean, Marie, Pierre]";
    }
    
    @Url("/api/etudiant")
    @Post
    public String ajouterEtudiant(String nom, int age) {
        return "SPRINT 7 POST : Etudiant ajoute - Nom: " + nom + ", Age: " + age;
    }
    
    // ========================================================================
    // TEST 3 : Même URL avec variables et paramètres
    // ========================================================================
    
    @Url("/api/etudiant/{id}")
    @Get
    public String getEtudiant(int id) {
        return "SPRINT 7 GET : Details de l'etudiant ID=" + id;
    }
    
    @Url("/api/etudiant/{id}")
    @Post
    public String updateEtudiant(int id, String nom, String email) {
        return "SPRINT 7 POST : Etudiant " + id + " mis a jour - Nom: " + nom + ", Email: " + email;
    }
    
    // ========================================================================
    // TEST 4 : Avec @RequestParam
    // ========================================================================
    
    @Url("/api/search")
    @Get
    public String searchGet(@RequestParam("query") String query) {
        return "SPRINT 7 GET : Recherche pour: " + query;
    }
    
    @Url("/api/search")
    @Post
    public String searchPost(@RequestParam("query") String query, @RequestParam("type") String type) {
        return "SPRINT 7 POST : Recherche avancee - Query: " + query + ", Type: " + type;
    }
    
    // ========================================================================
    // TEST 5 : DELETE et PUT (bonus si vous voulez étendre)
    // Pour l'instant on simule avec POST
    // ========================================================================
    
    @Url("/api/etudiant/{id}/delete")
    @Post
    public String deleteEtudiant(int id) {
        return "SPRINT 7 POST (DELETE) : Etudiant " + id + " supprime";
    }

    // ========================================================================
    // SPRINT 8 : Mapping automatique avec Map
    // ========================================================================
    
    // Afficher le formulaire simple
    @Url("/formulaire")
    @Get
    public framework.ModelAndView.ModelAndView afficherFormulaire() {
        return new framework.ModelAndView.ModelAndView("/views/Sprint-8/form-user.jsp");
    }
    
    // Test 1 : Map<String, Object> - Conversion automatique activee
    @Url("/test-object")
    @Get
    public String testMapObject(Map<String, Object> params) {
        StringBuilder result = new StringBuilder();
        result.append("=== Map<String, Object> - Conversion AUTO ===\n\n");
        result.append(params.toString()).append("\n\n");
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            String type = value != null ? value.getClass().getSimpleName() : "null";
            result.append(entry.getKey()).append(" : ").append(value)
                  .append(" (").append(type).append(")\n");
        }
        
        return result.toString();
    }
    
    // Test 2 : Map<String, String> - Type NON SUPPORTE
    @Url("/test-string")
    @Get
    public String testMapString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        result.append("=== Map<String, String> - Type NON SUPPORTE ===\n\n");
        
        if (params == null) {
            result.append(" RESULTAT : null\n\n");
            result.append("EXPLICATION :\n");
            result.append("Le framework ne supporte QUE Map<String, Object>\n");
            result.append("avec conversion automatique des types.\n\n");
            
        } else {
            result.append(" RESULTAT : ").append(params.toString()).append("\n\n");
            result.append("(Ce cas ne devrait pas arriver normalement)\n");
        }
        
        return result.toString();
    }
    
    // Test 3 : Map<String, Integer> - Type NON SUPPORTE
    @Url("/test-integer")
    @Get
    public String testMapInteger(Map<String, Integer> params) {
        StringBuilder result = new StringBuilder();
        result.append("=== Map<String, Integer> - Type NON SUPPORTE ===\n\n");
        
        if (params == null) {
            result.append(" RESULTAT : null\n\n");
            result.append("EXPLICATION :\n");
            result.append("Le framework ne supporte QUE :\n");
           
        } else {
            result.append("RESULTAT : ").append(params.toString()).append("\n\n");
            result.append("(Ce cas ne devrait pas arriver normalement)\n");
        }
        
        return result.toString();
    }
    
    // ========================================================================
    // SPRINT 8-BIS : Injection automatique d'objets personnalisés
    // ========================================================================
    
    // Afficher le formulaire employe
    @Url("/form-employe")
    @Get
    public framework.ModelAndView.ModelAndView afficherFormEmploye() {
        return new framework.ModelAndView.ModelAndView("/views/Sprint-8/form-employe.jsp");
    }
    
    // Sauvegarder un employé (POST via formulaire)
    @Url("/save-employe")
    @Post
    public String saveEmployePost(test.model.Employe emp) {
        return afficherDetailsEmploye(emp, "POST");
    }
    
    // Sauvegarder un employé (GET via URL pour test rapide)
    @Url("/save-employe")
    @Get
    public String saveEmployeGet(test.model.Employe emp) {
        return afficherDetailsEmploye(emp, "GET");
    }
    
    // Méthode commune pour afficher les détails
    private String afficherDetailsEmploye(test.model.Employe emp, String method) {
        StringBuilder result = new StringBuilder();
        result.append("=== SPRINT 8-BIS - INJECTION D'OBJET (").append(method).append(") ===\n\n");
        
        if (emp == null) {
            result.append(" Erreur : L'objet Employe est null\n");
        } else {
            result.append(" Objet Employe recu avec succes !\n\n");
            result.append("Détails de l'employé :\n");
            result.append("---------------------\n");
            result.append("Nom     : ").append(emp.getNom()).append("\n");
            result.append("Age     : ").append(emp.getAge()).append(" ans\n");
            result.append("Salaire : ").append(emp.getSalaire()).append(" €\n");
            result.append("Actif   : ").append(emp.getActif() != null ? (emp.getActif() ? "Oui" : "Non") : "null").append("\n\n");
            result.append("toString() : ").append(emp.toString()).append("\n\n");
            result.append("Types des attributs :\n");
            result.append("  nom     : ").append(emp.getNom() != null ? emp.getNom().getClass().getSimpleName() : "null").append("\n");
            result.append("  age     : ").append(emp.getAge() != null ? emp.getAge().getClass().getSimpleName() : "null").append("\n");
            result.append("  salaire : ").append(emp.getSalaire() != null ? emp.getSalaire().getClass().getSimpleName() : "null").append("\n");
            result.append("  actif   : ").append(emp.getActif() != null ? emp.getActif().getClass().getSimpleName() : "null").append("\n");
        }
        
        return result.toString();
    }

}