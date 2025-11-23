package framework.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private String view;  // Le nom de la vue (ex: "test.jsp")
    private Map<String, Object> model;

    public ModelAndView(String view) {
        this.view = view;
        this.model = new HashMap<>();
    }
    
    public String getView() {
        return view;
    }
    
    public void setView(String view) {
        this.view = view;
    }

    public void addObject(String key, Object value) {
        this.model.put(key, value);
    }
    
    public void addModel(String key, Object value) {
        this.model.put(key, value);
    }
    
    public Map<String, Object> getModel() {
        return model;
    }
    
    public Object getObject(String key) {
        return model.get(key);
    }
    
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}