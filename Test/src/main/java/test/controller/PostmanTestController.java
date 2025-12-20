package test.controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.Post;
import framework.annotation.RequestParam;
import framework.annotation.Url;
import test.model.Employe;
import test.model.Departement;

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
    
    // ========================================================================
    // SPRINT 8-BIS : Tests avec ARRAYS - Testables dans le navigateur (GET)
    // ========================================================================
    
    /**
     * Test 1 : Array de String comme paramètre direct (GET)
     * Exemple : GET /api/test/noms?noms=Jean,Marie,Pierre
     */
    @Url("/api/test/noms")
    @Get
    public String testArrayString(@RequestParam("noms") String[] noms) {
        StringBuilder result = new StringBuilder("SPRINT 8-BIS : Array de noms recus (" + noms.length + ") : [");
        for (int i = 0; i < noms.length; i++) {
            if (i > 0) result.append(", ");
            result.append(noms[i]);
        }
        result.append("]");
        return result.toString();
    }
    
    /**
     * Test 2 : Array de int comme paramètre direct (GET)
     * Exemple : GET /api/test/ages?ages=25,30,35,40,45
     */
    @Url("/api/test/ages")
    @Get
    public String testArrayInt(@RequestParam("ages") int[] ages) {
        int somme = 0;
        int min = ages[0];
        int max = ages[0];
        
        for (int age : ages) {
            somme += age;
            if (age < min) min = age;
            if (age > max) max = age;
        }
        
        double moyenne = (double) somme / ages.length;
        return "SPRINT 8-BIS : Analyse de " + ages.length + " ages - Min: " + min + ", Max: " + max + ", Moyenne: " + moyenne;
    }
    
    /**
     * Test 3 : Array de Double comme paramètre direct (GET)
     * Exemple : GET /api/test/salaires?salaires=2500.50,3200.75,2800.00
     */
    @Url("/api/test/salaires")
    @Get
    public String testArrayDouble(@RequestParam("salaires") double[] salaires) {
        double total = 0;
        for (double salaire : salaires) {
            total += salaire;
        }
        return "SPRINT 8-BIS : Total des salaires (" + salaires.length + " employes) : " + total + " EUR";
    }
    
    /**
     * Test 4 : Multiple arrays en même temps (GET)
     * Exemple : GET /api/test/multi?noms=Jean,Marie,Pierre&ages=30,25,35
     */
    @Url("/api/test/multi")
    @Get
    public String testMultipleArrays(@RequestParam("noms") String[] noms, @RequestParam("ages") int[] ages) {
        StringBuilder result = new StringBuilder("SPRINT 8-BIS : Employes : ");
        for (int i = 0; i < noms.length && i < ages.length; i++) {
            if (i > 0) result.append(", ");
            result.append(noms[i]).append(" (").append(ages[i]).append(" ans)");
        }
        return result.toString();
    }
    
    // ========================================================================
    // SPRINT 8-BIS : Tests avec objets personnalisés (Employe, Departement)
    // ========================================================================
    
    /**
     * Test 1 : Objet Employe simple (GET pour tester dans le navigateur)
     * Exemple : GET /api/employe?nom=Jean&age=30&salaire=2500.50&actif=true
     */
    @Url("/api/employe")
    @Get
    public String afficherEmploye(Employe employe) {
        return "SPRINT 8-BIS (GET) : Employe recu - " + employe.toString();
    }
    
    /**
     * Test 1bis : Objet Employe simple (POST)
     * Exemple : POST /api/employe?nom=Jean&age=30&salaire=2500.50&actif=true
     */
    @Url("/api/employe")
    @Post
    public String creerEmploye(Employe employe) {
        return "SPRINT 8-BIS (POST) : Employe cree - " + employe.toString();
    }
    
    /**
     * Test 2 : Objet Departement avec array de String (GET pour tester dans le navigateur)
     * Exemple : GET /api/departement?nom=IT&code=DEPT01&budget=50000&employes=Jean,Marie,Pierre
     */
    @Url("/api/departement")
    @Get
    public String afficherDepartement(Departement departement) {
        StringBuilder result = new StringBuilder("SPRINT 8-BIS (GET) : Departement recu - ");
        result.append(departement.toString());
        return result.toString();
    }
    
    /**
     * Test 2bis : Objet Departement avec array de String (POST)
     * Exemple : POST /api/departement?nom=IT&code=DEPT01&budget=50000&employes=Jean,Marie,Pierre
     */
    @Url("/api/departement")
    @Post
    public String creerDepartement(Departement departement) {
        StringBuilder result = new StringBuilder("SPRINT 8-BIS (POST) : Departement cree - ");
        result.append(departement.toString());
        return result.toString();
    }
    
    /**
     * Test 3 : Array de String comme paramètre direct
     * Exemple : POST /api/employes/batch?noms=Alice,Bob,Charlie
     */
    @Url("/api/employes/batch")
    @Post
    public String ajouterEmployesEnBatch(@RequestParam("noms") String[] noms) {
        StringBuilder result = new StringBuilder("SPRINT 8-BIS : Ajout en batch de " + noms.length + " employes : [");
        for (int i = 0; i < noms.length; i++) {
            if (i > 0) result.append(", ");
            result.append(noms[i]);
        }
        result.append("]");
        return result.toString();
    }
    
    /**
     * Test 4 : Array de int comme paramètre direct
     * Exemple : POST /api/employes/ages?ages=25,30,35,40
     */
    @Url("/api/employes/ages")
    @Post
    public String analyserAges(@RequestParam("ages") int[] ages) {
        int somme = 0;
        for (int age : ages) {
            somme += age;
        }
        double moyenne = (double) somme / ages.length;
        return "SPRINT 8-BIS : Analyse de " + ages.length + " ages - Moyenne : " + moyenne;
    }
    
    /**
     * Test 5 : Combinaison objet Employe + paramètres simples + array
     * Exemple : POST /api/employe/complet?tags=senior,manager,remote&note=Excellent employe
     */
    @Url("/api/employe/complet")
    @Post
    public String creerEmployeComplet(Employe employe, @RequestParam("tags") String[] tags, 
                                     @RequestParam("note") String note) {
        StringBuilder result = new StringBuilder("SPRINT 8-BIS : Employe cree avec details - ");
        result.append(employe.toString());
        result.append(" | Tags: [");
        for (int i = 0; i < tags.length; i++) {
            if (i > 0) result.append(", ");
            result.append(tags[i]);
        }
        result.append("] | Note: ").append(note);
        return result.toString();
    }
    
    /**
     * Test 6 : Departement avec Employe en paramètre séparé
     * Exemple : POST /api/departement/avec-responsable?nom=RH&code=DEPT02&budget=30000&employes=Alice,Bob
     *           &responsable.nom=Directeur&responsable.age=45&responsable.salaire=5000&responsable.actif=true
     * Note: Ce test nécessiterait une extension du framework pour supporter les objets imbriqués
     */
    @Url("/api/departement/simple")
    @Post
    public String creerDepartementSimple(Departement departement, @RequestParam("responsable") String responsable) {
        return "SPRINT 8-BIS : Departement cree - " + departement.toString() + 
               " | Responsable: " + responsable;
    }
}