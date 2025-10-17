# SPRINT 1 - bis

# 🚀 Mini Framework Servlet - Deployment

## 📋 Description
Framework Java Servlet avec un FrontServlet intelligent qui gère dynamiquement les URLs. Après optimisation, le FrontServlet :
- Affiche le chemin exact** tapé par l'utilisateur pour les URLs personnalisées
- **Sert automatiquement** tous les fichiers existants (HTML, JSP, CSS, images)
- **Gère parfaitement** les ressources dans tous les sous-dossiers
- **Utilise le servlet "default"** pour une gestion optimale des ressources statiques

## 🛠️ Déploiement Automatisé

### Structure du Projet
Project/
├── Framework/ # Module framework (JAR)
│ ├── src/
│ │ └── FrontServlet.java # FrontController optimisé
│ └── pom.xml
├── Test/ # Module test (WAR)
│ ├── src/
│ ├── webapp/ # Fichiers HTML/JSP de test
│ ├── lib/ # JAR du framework
│ └── pom.xml
└── deploy.bat # Script de déploiement one-click



### Script de Déploiement
Le script `deploy.bat` automatise complètement :
1. **Compilation** du framework en JAR avec Maven
2. **Copie automatique** du JAR dans `Test/lib/`
3. **Génération** du WAR de test avec dépendances
4. **Nettoyage** des anciens déploiements Tomcat
5. **Déploiement** sur Tomcat 10.1 avec redémarrage

### Fonctionnalités du FrontServlet Optimisé
-**Pattern `/*`** - Intercepte toutes les URLs
-**Affichage direct** des chemins personnalisés
-**Délégation au servlet default** pour les fichiers existants
-**Gestion native** de tous les types de fichiers
-**Code simplifié** et maintenable

## URLs Supportées
- `/` → Affiche "Chemin demandé : /"
- `/mon-chemin` → Affiche "/mon-chemin" 
- `/fichier.html` → Sert le fichier HTML automatiquement
- `/dossier/page.jsp` → Sert la page JSP depuis sous-dossier
- `/css/style.css` → Sert les ressources statiques

