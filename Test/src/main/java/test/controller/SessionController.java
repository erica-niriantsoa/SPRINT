package test.controller;

import framework.ModelAndView.ModelAndView;
import framework.annotation.Controller;
import framework.annotation.Url;
import framework.annotation.Get;
import framework.annotation.Post;
import framework.annotation.RequestParam;
import framework.annotation.Session;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * SPRINT 11 : Contrôleur de test pour la gestion de session
 * Test avec la couleur préférée de l'utilisateur
 * 
 * APPROCHE CORRECTE : Utilisation directe de la Map injectée via @Session
 */
@Controller
public class SessionController {
    
    /**
     * Affiche un formulaire pour choisir une couleur préférée
     */
    @Get
    @Url("/session/choix-couleur")
    public ModelAndView afficherFormulaireCouleur(@Session Map<String, Object> session) {
        ModelAndView mv = new ModelAndView("/views/Sprint-11/choix-couleur.jsp");
        
        // Récupérer la couleur actuelle de la session si elle existe
        String couleurActuelle = (String) session.get("couleurPreferee");
        int nbVisites = session.get("nbVisites") != null ? (int) session.get("nbVisites") : 0;
        
        // Incrémenter le nombre de visites DIRECTEMENT dans la session
        session.put("nbVisites", nbVisites + 1);
        
        mv.addObject("couleurActuelle", couleurActuelle);
        mv.addObject("nbVisites", nbVisites + 1);
        
        return mv;
    }
    
    /**
     * Enregistre la couleur choisie dans la session
     */
    @Post
    @Url("/session/enregistrer-couleur")
    public ModelAndView enregistrerCouleur(
            @Session Map<String, Object> session,
            @RequestParam("couleur") String couleur) {
        
        // Enregistrer la couleur DIRECTEMENT dans la session
        session.put("couleurPreferee", couleur);
        session.put("dernierChangement", System.currentTimeMillis());
        
        ModelAndView mv = new ModelAndView("/views/Sprint-11/resultat-couleur.jsp");
        mv.addObject("couleur", couleur);
        mv.addObject("message", "Couleur enregistrée avec succès !");
        
        return mv;
    }
    
    /**
     * Affiche toutes les données de session
     */
    @Get
    @Url("/session/afficher")
    public ModelAndView afficherSession(@Session Map<String, Object> session) {
        ModelAndView mv = new ModelAndView("/views/Sprint-11/afficher-session.jsp");
        mv.addObject("sessionData", session);
        return mv;
    }
    
    /**
     * Vide la session
     */
    @Get
    @Url("/session/vider")
    public ModelAndView viderSession(@Session Map<String, Object> session, HttpServletRequest request) {
        // Invalider complètement la session HTTP
        request.getSession().invalidate();
        
        ModelAndView mv = new ModelAndView("/views/Sprint-11/session-videe.jsp");
        mv.addObject("message", "Session vidée avec succès !");
        
        return mv;
    }
    
    /**
     * Test avec plusieurs valeurs dans la session
     */
    @Post
    @Url("/session/profil")
    public ModelAndView enregistrerProfil(
            @Session Map<String, Object> session,
            @RequestParam("nom") String nom,
            @RequestParam("age") int age,
            @RequestParam("couleur") String couleur) {
        
        // Enregistrer plusieurs données DIRECTEMENT dans la session
        session.put("nom", nom);
        session.put("age", age);
        session.put("couleurPreferee", couleur);
        session.put("profilComplet", true);
        
        ModelAndView mv = new ModelAndView("/views/Sprint-11/profil-enregistre.jsp");
        mv.addObject("nom", nom);
        mv.addObject("age", age);
        mv.addObject("couleur", couleur);
        
        return mv;
    }
}