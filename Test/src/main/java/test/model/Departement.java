package test.model;

/**
 * SPRINT 8-BIS (étendu) : Classe Departement pour les tests avec arrays
 */
public class Departement {
    private String nom;
    private String code;
    private Integer budget;
    private String[] employes; // Tableau de noms d'employés
    
    // Constructeur par défaut obligatoire pour la reflection
    public Departement() {
    }
    
    // Getters
    public String getNom() {
        return nom;
    }
    
    public String getCode() {
        return code;
    }
    
    public Integer getBudget() {
        return budget;
    }
    
    public String[] getEmployes() {
        return employes;
    }
    
    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public void setBudget(Integer budget) {
        this.budget = budget;
    }
    
    public void setEmployes(String[] employes) {
        this.employes = employes;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Departement{")
          .append("nom='").append(nom).append('\'')
          .append(", code='").append(code).append('\'')
          .append(", budget=").append(budget)
          .append(", employes=[");
        
        if (employes != null) {
            for (int i = 0; i < employes.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(employes[i]);
            }
        }
        sb.append("]}");
        
        return sb.toString();
    }
}
