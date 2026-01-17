# SPRINT 11 - GESTION DE SESSION
## Framework MVC personnalisé avec support de session

---

## 🎯 Objectif du Sprint

Implémenter un système de gestion de session HTTP avec conversion automatique en `Map<String, Object>` pour faciliter la manipulation des données de session dans les contrôleurs.

---

## 📋 Fonctionnalités Implémentées

### 1. **Annotation @Session**
- Nouvelle annotation pour injecter automatiquement la session dans les méthodes de contrôleur
- Localisation : `framework.annotation.Session`
- Cible : Paramètres de méthode uniquement
- Type attendu : `Map<String, Object>`

### 2. **Copie Bidirectionnelle HttpSession ↔ Map**
- **Extraction** : Conversion de `HttpSession` vers `Map<String, Object>` avant l'invocation
- **Synchronisation** : Mise à jour automatique de `HttpSession` après l'invocation
- Gestion transparente pour le développeur

### 3. **Injection Automatique dans FrameworkDispatcher**
- Détection automatique de l'annotation `@Session`
- Création d'une copie de la session HTTP en Map
- Injection du Map dans le paramètre annoté
- Synchronisation des modifications après l'exécution

---

## 🏗️ Architecture Technique

### Flux de Traitement

```
1. Requête HTTP arrive au FrontServlet
2. FrameworkDispatcher.prepareMethodArguments()
   ├─> Détecte @Session sur un paramètre
   ├─> Appelle extractSessionAsMap()
   │   └─> Parcourt tous les attributs de HttpSession
   │       └─> Crée Map<String, Object> avec les données
   └─> Injecte le Map dans le paramètre
3. Méthode de contrôleur s'exécute
   └─> Peut lire/modifier le Map librement
4. FrameworkDispatcher.syncSessionBack()
   └─> Parcourt le Map modifié
       └─> Met à jour HttpSession avec les nouvelles valeurs
5. Réponse envoyée au client
```

### Classes Modifiées

#### **1. Session.java** (nouvelle)
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Session {
}
```

#### **2. FrameworkDispatcher.java**
**Méthodes ajoutées :**
- `extractSessionAsMap(HttpServletRequest)` : Convertit HttpSession → Map
- `syncSessionBack(HttpServletRequest, Object[], Parameter[])` : Synchronise Map → HttpSession

**Méthodes modifiées :**
- `prepareMethodArguments()` : Gère l'injection @Session
- `invokeControllerMethod()` : Appelle syncSessionBack après invocation

---

## 💻 Utilisation

### Dans un Contrôleur

```java
@Controller
public class SessionController {
    
    @Get("/session/choix-couleur")
    public ModelAndView afficherFormulaire(@Session Map<String, Object> session) {
        // Lecture de la session
        String couleur = (String) session.get("couleurPreferee");
        int nbVisites = session.get("nbVisites") != null ? 
                        (int) session.get("nbVisites") : 0;
        
        // Modification de la session
        session.put("nbVisites", nbVisites + 1);
        
        ModelAndView mv = new ModelAndView("/views/choix-couleur.jsp");
        mv.addObject("couleurActuelle", couleur);
        mv.addObject("nbVisites", nbVisites + 1);
        return mv;
    }
    
    @Post("/session/enregistrer-couleur")
    public ModelAndView enregistrer(
            @Session Map<String, Object> session,
            @RequestParam("couleur") String couleur) {
        
        // Enregistrement dans la session
        session.put("couleurPreferee", couleur);
        session.put("dernierChangement", System.currentTimeMillis());
        
        return new ModelAndView("/views/resultat.jsp");
    }
    
    @Get("/session/vider")
    public ModelAndView vider(@Session Map<String, Object> session) {
        // Vider la session
        session.clear();
        return new ModelAndView("/views/session-videe.jsp");
    }
}
```

---

## 🧪 Tests Implémentés

### Test 1 : Couleur Préférée
**URL :** `/session/choix-couleur`
- Affiche un formulaire avec plusieurs couleurs
- Stocke la couleur choisie en session
- Le fond de page change selon la couleur préférée
- Compte le nombre de visites

### Test 2 : Affichage de Session
**URL :** `/session/afficher`
- Affiche toutes les données en session
- Format tableau avec clé, valeur, type
- Utile pour déboguer

### Test 3 : Vider la Session
**URL :** `/session/vider`
- Supprime toutes les données de session
- Message de confirmation

### Test 4 : Profil Complet
**URL :** `/session/profil` (POST)
- Enregistre plusieurs données : nom, âge, couleur
- Démontre le stockage multiple

---

## 📂 Structure des Fichiers

```
Framework/src/main/java/framework/
├── annotation/
│   └── Session.java                    [NOUVEAU]
├── dispatcher/
│   └── FrameworkDispatcher.java        [MODIFIÉ]

Test/src/main/java/test/
└── controller/
    └── SessionController.java          [NOUVEAU]

Test/src/main/webapp/views/Sprint-11/   [NOUVEAU]
├── choix-couleur.jsp
├── resultat-couleur.jsp
├── afficher-session.jsp
├── session-videe.jsp
└── profil-enregistre.jsp
```

---

## 🚀 Déploiement et Test

### 1. Compilation
```bash
cd Framework
mvn clean install

cd ../Test
mvn clean package
```

### 2. Déploiement
```bash
# Copier le WAR dans Tomcat
copy Test\target\Test.war %CATALINA_HOME%\webapps\

# Démarrer Tomcat
%CATALINA_HOME%\bin\startup.bat
```

### 3. Tests
Ouvrir dans le navigateur :
- http://localhost:8080/Test/session/choix-couleur
- http://localhost:8080/Test/session/afficher

---

## 🔍 Points Techniques Importants

### 1. Copie de Session
```java
private static Map<String, Object> extractSessionAsMap(HttpServletRequest request) {
    Map<String, Object> sessionMap = new HashMap<>();
    HttpSession httpSession = request.getSession(true);
    
    Enumeration<String> attributeNames = httpSession.getAttributeNames();
    while (attributeNames.hasMoreElements()) {
        String key = attributeNames.nextElement();
        Object value = httpSession.getAttribute(key);
        sessionMap.put(key, value);
    }
    
    return sessionMap;
}
```

### 2. Synchronisation Bidirectionnelle
```java
private static void syncSessionBack(HttpServletRequest request, 
                                    Object[] methodArgs, 
                                    Parameter[] parameters) {
    HttpSession httpSession = request.getSession(false);
    if (httpSession == null) return;
    
    for (int i = 0; i < parameters.length; i++) {
        Parameter param = parameters[i];
        if (param.isAnnotationPresent(Session.class) && 
            methodArgs[i] instanceof Map) {
            
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) methodArgs[i];
            
            // Synchroniser vers HttpSession
            for (Map.Entry<String, Object> entry : sessionMap.entrySet()) {
                httpSession.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }
}
```

### 3. Gestion du Typage
- Le paramètre doit être de type `Map<String, Object>`
- Les objets stockés doivent être `Serializable` (bonne pratique)
- Cast nécessaire lors de la récupération des valeurs

---

## ⚠️ Considérations de Sécurité

### 1. Validation des Données
- Toujours valider les données avant de les stocker en session
- Ne pas stocker de données sensibles (mots de passe, etc.)

### 2. Sérialisation
- Les objets stockés doivent implémenter `Serializable` pour les sessions distribuées
- Attention à la taille des objets (impact mémoire)

### 3. Timeout
- Configurer le timeout de session dans `web.xml` :
```xml
<session-config>
    <session-timeout>30</session-timeout> <!-- 30 minutes -->
</session-config>
```

### 4. Nettoyage
- Utiliser `session.clear()` ou supprimer les attributs inutiles
- Éviter de stocker trop de données en session

---

## 🎨 Exemple Complet : Couleur Préférée

### Contrôleur
```java
@Get("/session/choix-couleur")
public ModelAndView afficherFormulaire(@Session Map<String, Object> session) {
    String couleur = (String) session.get("couleurPreferee");
    int nbVisites = session.get("nbVisites") != null ? 
                    (int) session.get("nbVisites") : 0;
    
    session.put("nbVisites", nbVisites + 1);
    
    ModelAndView mv = new ModelAndView("/views/Sprint-11/choix-couleur.jsp");
    mv.addObject("couleurActuelle", couleur);
    mv.addObject("nbVisites", nbVisites + 1);
    return mv;
}

@Post("/session/enregistrer-couleur")
public ModelAndView enregistrer(
        @Session Map<String, Object> session,
        @RequestParam("couleur") String couleur) {
    
    session.put("couleurPreferee", couleur);
    session.put("dernierChangement", System.currentTimeMillis());
    
    ModelAndView mv = new ModelAndView("/views/Sprint-11/resultat-couleur.jsp");
    mv.addObject("couleur", couleur);
    return mv;
}
```

### JSP (choix-couleur.jsp)
```jsp
<body style="background-color: ${couleurActuelle}">
    <h1>Choisir votre Couleur Préférée</h1>
    <p>Nombre de visites : ${nbVisites}</p>
    
    <form method="post" action="/Test/session/enregistrer-couleur">
        <input type="radio" name="couleur" value="#FF6B6B"> Rouge
        <input type="radio" name="couleur" value="#4ECDC4"> Turquoise
        <button type="submit">Enregistrer</button>
    </form>
</body>
```

---

## 📊 Avantages de l'Approche

✅ **Simplicité** : Map au lieu de HttpSession directement  
✅ **Type-safe** : Utilisation de génériques Java  
✅ **Transparent** : Synchronisation automatique  
✅ **Flexible** : Fonctionne avec n'importe quel type d'objet  
✅ **Testable** : Facile à mocker pour les tests unitaires  

---

## 🔄 Améliorations Futures Possibles

1. **Session Scoped Beans** : Support d'objets avec portée session
2. **Flash Scope** : Données disponibles uniquement pour la prochaine requête
3. **Session Attributes** : Annotation pour des attributs spécifiques
4. **Session Events** : Listeners pour création/destruction de session
5. **Distributed Sessions** : Support Redis/Memcached pour clustering

---

## 📝 Notes de Version

**Version :** Sprint 11  
**Date :** Janvier 2026  
**Compatibilité :** Jakarta EE 10, Java 17+  
**Sprints précédents requis :** Sprint 1-10

---

## 👤 Auteur

Framework MVC personnalisé - Projet académique  
Technologie d'Accès Réseau - Semestre 5

---

**🎉 Sprint 11 terminé avec succès !**
