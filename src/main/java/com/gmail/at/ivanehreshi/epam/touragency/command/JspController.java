package com.gmail.at.ivanehreshi.epam.touragency.command;

public class JspController extends Controller {
    private String prefix = ".jsp";
    private String location = "/pages/";
    
    public JspController() {
    }
    
    public JspController(String location, String prefix) {
        this.prefix = prefix;
        this.location = location;
    }
    
    @Override
    public void get(RequestService service) {
        String page = service.getPathParameters().get(0) + prefix;
        service.renderPage(location + page);
    }

    @Override
    public boolean isService() {
        return true;
    }

}
