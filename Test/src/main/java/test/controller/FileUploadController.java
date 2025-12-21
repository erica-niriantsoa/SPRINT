package test.controller;

import framework.annotation.*;
import framework.ModelAndView.ModelAndView;
import framework.upload.FileUpload;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SPRINT 10 : Contrôleur pour tester l'upload de fichiers avec sauvegarde
 */
@Controller
public class FileUploadController {

    /**
     * Affiche le formulaire d'upload
     */
    @Get
    @Url("/upload")
    public ModelAndView showUploadForm() {
        return new ModelAndView("/views/Sprint-10/upload-form.jsp");
    }

    /**
     * Traite l'upload d'un seul fichier et le sauvegarde dans Downloads avec extension originale
     */
    @Post
    @Url("/upload/single")
    public ModelAndView uploadSingleFile(@FileParam("fichier") FileUpload fichier, 
                                         @RequestParam("titre") String titre,
                                         @RequestParam("description") String description) {
        ModelAndView mv = new ModelAndView("/views/Sprint-10/upload-result.jsp");
        
        if (fichier != null && fichier.getContent() != null) {
            try {
                // Dossier de sauvegarde : uploads dans le projet (Tomcat a les droits)
                String downloadPath = "D:\\cours\\S5\\TECHNO ACCES RESEAU\\SPRINT\\uploads";
                Path uploadDir = Paths.get(downloadPath);
                
                // Créer le dossier s'il n'existe pas
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                
                // Générer un nom de fichier : utiliser le nom original
                String fileName = fichier.getFileName();
                Path filePath = uploadDir.resolve(fileName);
                
                // Sauvegarder le fichier
                Files.write(filePath, fichier.getContent());
                
                mv.addObject("success", true);
                mv.addObject("titre", titre);
                mv.addObject("description", description);
                mv.addObject("fileSize", fichier.getSize());
                mv.addObject("fileSizeKB", fichier.getSize() / 1024.0);
                mv.addObject("savedPath", filePath.toString());
                mv.addObject("fileName", fileName);
                mv.addObject("originalFileName", fichier.getFileName());
                mv.addObject("contentType", fichier.getContentType());
                mv.addObject("message", "Fichier uploadé et sauvegardé avec succès !");
            } catch (IOException e) {
                mv.addObject("success", false);
                mv.addObject("message", "Erreur lors de la sauvegarde : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mv.addObject("success", false);
            mv.addObject("message", "Aucun fichier n'a été uploadé.");
        }
        
        return mv;
    }

    /**
     * Traite l'upload de plusieurs fichiers et les sauvegarde dans Downloads
     */
    @Post
    @Url("/upload/multiple")
    public ModelAndView uploadMultipleFiles(@FileParam("photo") FileUpload photo,
                                            @FileParam("cv") FileUpload cv,
                                            @RequestParam("nom") String nom,
                                            @RequestParam("prenom") String prenom) {
        ModelAndView mv = new ModelAndView("/views/Sprint-10/upload-multiple-result.jsp");
        
        mv.addObject("nom", nom);
        mv.addObject("prenom", prenom);
        
        try {
            String downloadPath = "D:\\cours\\S5\\TECHNO ACCES RESEAU\\SPRINT\\uploads";
            Path uploadDir = Paths.get(downloadPath);
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Sauvegarder la photo avec son nom original
            if (photo != null && photo.getContent() != null) {
                String photoFileName = photo.getFileName();
                Path photoPath = uploadDir.resolve(photoFileName);
                Files.write(photoPath, photo.getContent());
                
                mv.addObject("photoSize", photo.getSize());
                mv.addObject("photoSizeKB", photo.getSize() / 1024.0);
                mv.addObject("photoPath", photoPath.toString());
                mv.addObject("photoFileName", photoFileName);
                mv.addObject("photoOriginalName", photo.getFileName());
            }
            
            // Sauvegarder le CV avec son nom original
            if (cv != null && cv.getContent() != null) {
                String cvFileName = cv.getFileName();
                Path cvPath = uploadDir.resolve(cvFileName);
                Files.write(cvPath, cv.getContent());
                
                mv.addObject("cvSize", cv.getSize());
                mv.addObject("cvSizeKB", cv.getSize() / 1024.0);
                mv.addObject("cvPath", cvPath.toString());
                mv.addObject("cvFileName", cvFileName);
                mv.addObject("cvOriginalName", cv.getFileName());
            }
            
            mv.addObject("message", "Fichiers uploadés et sauvegardés avec succès !");
            mv.addObject("savedLocation", downloadPath);
            
        } catch (IOException e) {
            mv.addObject("error", "Erreur lors de la sauvegarde : " + e.getMessage());
        }
        
        return mv;
    }

    /**
     * API REST pour l'upload de fichiers avec sauvegarde
     */
    @Post
    @Url("/api/upload")
    @RestAPI
    public Object uploadFileApi(@FileParam("fichier") FileUpload fichier,
                                @RequestParam("nom") String nom) {
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        if (fichier != null && fichier.getContent() != null) {
            try {
                String downloadPath = "D:\\cours\\S5\\TECHNO ACCES RESEAU\\SPRINT\\uploads";
                Path uploadDir = Paths.get(downloadPath);
                
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                // Utiliser le nom original du fichier
                String fileName = fichier.getFileName();
                Path filePath = uploadDir.resolve(fileName);
                
                Files.write(filePath, fichier.getContent());
                
                result.put("success", true);
                result.put("nom", nom);
                result.put("originalFileName", fichier.getFileName());
                result.put("contentType", fichier.getContentType());
                result.put("fileSize", fichier.getSize());
                result.put("fileSizeKB", fichier.getSize() / 1024.0);
                result.put("savedPath", filePath.toString());
                result.put("fileName", fileName);
                result.put("message", "Fichier uploadé et sauvegardé avec succès via API");
            } catch (IOException e) {
                result.put("success", false);
                result.put("message", "Erreur lors de la sauvegarde : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            result.put("success", false);
            result.put("message", "Aucun fichier reçu");
        }
        
        return result;
    }
}
