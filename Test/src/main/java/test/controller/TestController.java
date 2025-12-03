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

}