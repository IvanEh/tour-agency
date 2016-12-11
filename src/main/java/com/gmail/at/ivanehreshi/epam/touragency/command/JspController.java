package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;

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
    public void get(RequestService reqService) {
        String page = reqService.getPathParameters().get(0) + prefix;
        reqService.renderPage(location + page);
    }

    @Override
    public boolean isService() {
        return true;
    }

}
