# 🚀 SPRINT 10 - Upload de Fichiers

## 📋 Objectif
Permettre l'upload de fichiers dans les formulaires et l'injection automatique des fichiers uploadés dans les méthodes des contrôleurs via l'annotation `@FileParam`.

## ✅ Fonctionnalités Implémentées

### 1. Annotation @FileParam
Nouvelle annotation pour marquer les paramètres de type fichier dans les méthodes des contrôleurs :

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FileParam {
    String value();           // Nom du champ file dans le formulaire
    boolean required() default false;
}
```

### 2. Configuration Multipart dans FrontServlet
Le `FrontServlet` est maintenant annoté avec `@MultipartConfig` pour activer le support des fichiers uploadés :

```java
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 10,      // 10 MB max par fichier
    maxRequestSize = 1024 * 1024 * 50,   // 50 MB max pour la requête complète
    fileSizeThreshold = 1024 * 1024      // 1 MB avant d'écrire sur disque
)
public class FrontServlet extends HttpServlet {
    // ...
}
```

### 3. Extraction des Fichiers Uploadés
Méthode `extractUploadedFiles()` dans `FrameworkDispatcher` qui :
- Vérifie si la requête est de type `multipart/form-data`
- Utilise `request.getParts()` pour récupérer tous les fichiers
- Lit chaque fichier et le convertit en `byte[]`
- Stocke les fichiers dans une Map<String, byte[]>

```java
private static Map<String, byte[]> extractUploadedFiles(HttpServletRequest request) {
    Map<String, byte[]> files = new HashMap<>();
    
    if (request.getContentType() != null && 
        request.getContentType().toLowerCase().startsWith("multipart/form-data")) {
        
        Collection<Part> parts = request.getParts();
        
        for (Part part : parts) {
            if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                String fieldName = part.getName();
                try (InputStream inputStream = part.getInputStream()) {
                    byte[] fileContent = inputStream.readAllBytes();
                    files.put(fieldName, fileContent);
                }
            }
        }
    }
    return files;
}
```

### 4. Injection des Fichiers dans les Contrôleurs
La méthode `prepareMethodArguments()` a été modifiée pour détecter l'annotation `@FileParam` et injecter le fichier correspondant :

```java
// Dans prepareMethodArguments()
Map<String, byte[]> uploadedFiles = extractUploadedFiles(request);

for (int i = 0; i < parameters.length; i++) {
    Parameter param = parameters[i];
    
    // SPRINT 10 : @FileParam - Fichier uploadé
    if (param.isAnnotationPresent(FileParam.class)) {
        FileParam annotation = param.getAnnotation(FileParam.class);
        String fileName = annotation.value();
        args[i] = uploadedFiles.get(fileName);
        continue;
    }
    // ... autres types de paramètres
}
```

## 📝 Exemple d'Utilisation

### Upload d'un fichier simple
```java
@Post
@Url("/upload/single")
public ModelAndView uploadSingleFile(@FileParam("fichier") byte[] fichier, 
                                     @RequestParam("titre") String titre,
                                     @RequestParam("description") String description) {
    ModelAndView mv = new ModelAndView("/views/Sprint-10/upload-result.jsp");
    
    if (fichier != null) {
        mv.addObject("fileSize", fichier.length);
        mv.addObject("fileSizeKB", fichier.length / 1024.0);
        mv.addObject("message", "Fichier uploadé avec succès !");
    }
    
    return mv;
}
```

### Upload de plusieurs fichiers
```java
@Post
@Url("/upload/multiple")
public ModelAndView uploadMultipleFiles(@FileParam("photo") byte[] photo,
                                        @FileParam("cv") byte[] cv,
                                        @RequestParam("nom") String nom,
                                        @RequestParam("prenom") String prenom) {
    // Traitement des fichiers...
}
```

### API REST avec upload
```java
@Post
@Url("/api/upload")
@RestAPI
public Object uploadFileApi(@FileParam("fichier") byte[] fichier,
                            @RequestParam("nom") String nom) {
    Map<String, Object> result = new HashMap<>();
    if (fichier != null) {
        result.put("success", true);
        result.put("fileSize", fichier.length);
    }
    return result;
}
```

## 🎨 Formulaires HTML

### Formulaire simple
```html
<form action="/upload/single" method="POST" enctype="multipart/form-data">
    <input type="text" name="titre" required>
    <textarea name="description" required></textarea>
    <input type="file" name="fichier" required>
    <button type="submit">Envoyer</button>
</form>
```

### Formulaire avec plusieurs fichiers
```html
<form action="/upload/multiple" method="POST" enctype="multipart/form-data">
    <input type="text" name="nom" required>
    <input type="text" name="prenom" required>
    <input type="file" name="photo" accept="image/*">
    <input type="file" name="cv" accept=".pdf">
    <button type="submit">Envoyer</button>
</form>
```

### Upload via AJAX/Fetch
```javascript
const formData = new FormData();
formData.append('nom', 'Document Test');
formData.append('fichier', fileInput.files[0]);

fetch('/api/upload', {
    method: 'POST',
    body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

## 🧪 Tests

Pour tester le Sprint 10 :

1. **Compiler le projet** :
   ```bash
   cd Framework
   mvn clean install
   
   cd ../Test
   mvn clean package
   ```

2. **Déployer sur Tomcat**

3. **Accéder au formulaire** :
   - URL : `http://localhost:8080/Test/upload`
   - Tester l'upload d'un fichier simple
   - Tester l'upload de plusieurs fichiers
   - Tester l'API REST avec AJAX

## 🔧 Points Techniques

### Flux de données
1. Le formulaire HTML envoie les données avec `enctype="multipart/form-data"`
2. Le `FrontServlet` reçoit la requête (grâce à `@MultipartConfig`)
3. Le `FrameworkDispatcher` appelle `extractUploadedFiles()`
4. Les fichiers sont lus avec `getPart()` et convertis en `byte[]`
5. Les fichiers sont stockés dans une Map<String, byte[]>
6. Lors de l'injection des paramètres, `@FileParam` récupère le fichier correspondant
7. Le contrôleur reçoit le fichier sous forme de `byte[]`

### Avantages
- ✅ Simple à utiliser (une seule annotation)
- ✅ Injection automatique comme les autres paramètres
- ✅ Support de plusieurs fichiers
- ✅ Compatible avec REST API
- ✅ Pas de bibliothèque externe nécessaire

### Limitations
- Les fichiers sont chargés en mémoire (byte[])
- Pour de très gros fichiers, préférer le traitement par stream
- Pas de gestion du nom du fichier original (peut être ajouté si nécessaire)

## 📚 Fichiers Modifiés

1. **Framework/src/main/java/framework/annotation/FileParam.java** (nouveau)
2. **Framework/src/main/java/framework/servlet/FrontServlet.java** (modifié - @MultipartConfig)
3. **Framework/src/main/java/framework/dispatcher/FrameworkDispatcher.java** (modifié - extraction et injection)
4. **Test/src/main/java/test/controller/FileUploadController.java** (nouveau)
5. **Test/src/main/webapp/views/Sprint-10/upload-form.jsp** (nouveau)
6. **Test/src/main/webapp/views/Sprint-10/upload-result.jsp** (nouveau)
7. **Test/src/main/webapp/views/Sprint-10/upload-multiple-result.jsp** (nouveau)

## 🎯 Prochaines Améliorations Possibles

- Récupération du nom de fichier original
- Récupération du content-type
- Support des métadonnées de fichier
- Validation du type de fichier
- Limitation de la taille par fichier
- Stockage automatique sur disque
- Génération d'URL pour accéder aux fichiers uploadés
