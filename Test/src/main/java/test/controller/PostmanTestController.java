package test.controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.Post;
import framework.annotation.RequestParam;
import framework.annotation.Url;

@Controller
public class PostmanTestController {
    
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
}