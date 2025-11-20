package framework.ModelAndView;

public class ModelAndView {
    private String view;  // Le nom de la vue (ex: "test.jsp")
    
    public ModelAndView(String view) {
        this.view = view;
    }
    
    public String getView() {
        return view;
    }
    
    public void setView(String view) {
        this.view = view;
    }
}