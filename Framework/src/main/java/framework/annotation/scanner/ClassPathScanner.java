package framework.annotation.scanner;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.lang.annotation.Annotation;

/**
 * Scanner générique qui trouve TOUTES les classes avec une annotation
 */
public class ClassPathScanner {
    
    
    public List<Class<?>> findAnnotatedClasses(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        try {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader().getResources(path);
            
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File directory = new File(resource.getFile());
                    scanDirectory(packageName, directory, annotatedClasses, annotationClass);
                }
            }
        } catch (Exception e) {
            System.out.println(" Erreur scan: " + e.getMessage());
        }
        return annotatedClasses;
    }
    
    private void scanDirectory(String packageName, File directory, 
                         List<Class<?>> annotatedClasses, Class<? extends Annotation> annotationClass) {
    
        try {
            // CORRECTION POUR WINDOWS : décoder les URLs
            String windowsPath = java.net.URLDecoder.decode(directory.getPath(), "UTF-8");
            File windowsDirectory = new File(windowsPath);
            
            if (!windowsDirectory.exists()) {
                System.out.println(" Dossier non trouvé: " + windowsPath);
                return;
            }
            
            File[] files = windowsDirectory.listFiles();
            if (files == null) {
                System.out.println(" Aucun fichier dans: " + windowsPath);
                return;
            }
            
            System.out.println(" Dossier trouvé: " + windowsPath);
            System.out.println(" Fichiers: " + files.length);
            
            for (File file : files) {
                System.out.println(" " + file.getName());
                if (file.isDirectory()) {
                    scanDirectory(packageName + "." + file.getName(), file, annotatedClasses, annotationClass);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    System.out.println("   Chargement: " + className);
                    loadAndCheckAnnotation(className, annotatedClasses, annotationClass);
                }
            }
        } catch (Exception e) {
            System.out.println(" Erreur scan: " + e.getMessage());
        }
    }
    
    private void loadAndCheckAnnotation(String className, List<Class<?>> annotatedClasses, Class<? extends Annotation> annotationClass) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(annotationClass)) {
                annotatedClasses.add(clazz);
                System.out.println("Classe annotée trouvée: " + clazz.getSimpleName());
            }
        } catch (Exception e) {
            
        }
    }
}