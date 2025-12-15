package framework.response;

/**
 * SPRINT 9 : Classe pour standardiser les réponses JSON des API REST
 * 
 * Format de réponse :
 * {
 *   "status": "success" ou "error",
 *   "code": 200, 400, 404, 500, etc.
 *   "data": { ... },
 *   "message": "Message optionnel en cas d'erreur"
 * }
 */
public class ApiResponse {
    private String status;
    private int code;
    private Object data;
    private String message;
    
    public ApiResponse() {
    }
    
    public ApiResponse(String status, int code, Object data, String message) {
        this.status = status;
        this.code = code;
        this.data = data;
        this.message = message;
    }
    
    /**
     * Crée une réponse de succès (200)
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse("success", 200, data, null);
    }
    
    /**
     * Crée une réponse de succès avec code personnalisé
     */
    public static ApiResponse success(int code, Object data) {
        return new ApiResponse("success", code, data, null);
    }
    
    /**
     * Crée une réponse d'erreur
     */
    public static ApiResponse error(int code, String message) {
        return new ApiResponse("error", code, null, message);
    }
    
    /**
     * Crée une réponse d'erreur avec données
     */
    public static ApiResponse error(int code, String message, Object data) {
        return new ApiResponse("error", code, data, message);
    }
    
    // Getters et Setters
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
