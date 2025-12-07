package test.model;

/**
 * SPRINT 8-BIS : Classe de test pour l'injection automatique d'objets
 * Le framework remplit automatiquement les attributs via reflection
 */
public class Employe {
    private String nom;
    private Integer age;
    private Double salaire;
    private Boolean actif;
    
    // Constructeur par défaut obligatoire pour la reflection
    public Employe() {
    }
    
    // Getters
    public String getNom() {
        return nom;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public Double getSalaire() {
        return salaire;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    
    @Override
    public String toString() {
        return "Employe{" +
                "nom='" + nom + '\'' +
                ", age=" + age +
                ", salaire=" + salaire +
                ", actif=" + actif +
                '}';
    }
}
