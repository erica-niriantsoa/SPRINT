package framework.upload;

/**
 * SPRINT 10 : Représente un fichier uploadé avec toutes ses métadonnées
 */
public class FileUpload {
    private String fileName;        // Nom original du fichier
    private String contentType;     // Type MIME (image/jpeg, application/pdf, etc.)
    private byte[] content;         // Contenu du fichier en bytes
    private long size;              // Taille en octets

    public FileUpload(String fileName, String contentType, byte[] content) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
        this.size = content != null ? content.length : 0;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public long getSize() {
        return size;
    }

    /**
     * Retourne l'extension du fichier (ex: .jpg, .pdf)
     */
    public String getExtension() {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    /**
     * Retourne le nom sans extension
     */
    public String getFileNameWithoutExtension() {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }
}
