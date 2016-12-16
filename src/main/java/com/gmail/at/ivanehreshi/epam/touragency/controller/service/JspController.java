package com.gmail.at.ivanehreshi.epam.touragency.controller.service;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;

/**
 * Redirects incoming request to JSP pages
 *
 * <p>Used for allowing ControllerDispatcherServlet to redirect request
 * to JSP pages
 *
 * <p>Note that location and ControllerDispatcherServlet mapping should not
 * overlap and a StaticResourceFilter should be used to join the two
 *
 * @see com.gmail.at.ivanehreshi.epam.touragency.dispatcher.ControllerDispatcherServlet
 * @see com.gmail.at.ivanehreshi.epam.touragency.filter.StaticResourceFilter
 */
public final class JspController extends Controller {
    private String prefix = ".jsp";
    private String location = "/pages/";
    
    public JspController() {
    }
    
    public JspController(String loc, String pref) {
        this.prefix = pref;
        this.location = loc;
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
