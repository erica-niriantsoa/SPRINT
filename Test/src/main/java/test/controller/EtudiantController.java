package test.controller;

import framework.annotation.Controller;
import framework.annotation.RequestParam;
import framework.annotation.Url;
import framework.ModelAndView.ModelAndView;
import java.util.Arrays;

@Controller
public class EtudiantController {
    
    // ========================================================================
    // SPRINT 6 : Variable URL {id}
    // ========================================================================
    @Url("/etudiant/{id}")
    public String getEtudiant(int id) {
        return "SPRINT 6 : Etudiant avec ID: " + id + " (extrait de l'URL)";
    }
    
    // ========================================================================
    // SPRINT 6 : Multiples variables URL
    // ========================================================================
    @Url("/etudiant/{id}/notes/{matiere}")
    public String getNote(int id, String matiere) {
        return "SPRINT 6 : Notes de l'etudiant " + id + " en " + matiere;
    }

    // ========================================================================
    // SPRINT 6 + ModelAndView
    // ========================================================================
    @Url("/etudiant/{id}/profil")
    public ModelAndView getProfilEtudiant(int id) {
        ModelAndView mv = new ModelAndView("/views/etudiant-profil.jsp");
        
        mv.addObject("etudiantId", id);
        mv.addObject("nom", "Jean Dupont");
        mv.addObject("email", "jean.dupont@example.com");
        mv.addObject("matieres", Arrays.asList("Math", "Physique", "Informatique"));
        
        return mv;
    }
    
    // ========================================================================
    // SPRINT 6-bis : Convention de noms avec query params
    // ========================================================================
    @Url("/recherche-etudiant")
    public String rechercheEtudiant(String nom, int age) {
        return "SPRINT 6-bis : Recherche nom=" + nom + ", age=" + age;
    }
}