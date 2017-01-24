package com.gmail.at.ivanehreshi.epam.touragency.controller.service;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;

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
    private String oldSuffix = ".html";
    private String suffix = ".jsp";
    private String location = "/pages/";
    
    public JspController() {
    }
    
    public JspController(String loc, String oldSuff, String suff) {
        this.location = loc;
        this.oldSuffix = oldSuff;
        this.suffix = suff;

        fixStrings();
    }

    private void fixStrings() {
        if(oldSuffix.charAt(0) != '.') {
            oldSuffix = "." + oldSuffix;
        }

        if(location.charAt(location.length() - 1) != '/') {
            location = location + "/";
        }
    }

    @Override
    public void get(RequestService reqService) {
        String page = reqService.getRequest().getPathInfo().substring(1);
        int pos = page.indexOf(oldSuffix);
        if(pos != -1 && pos + oldSuffix.length() == page.length()) {
            page = page.substring(0, pos);
            reqService.renderPage(location + page + suffix);
        }
    }

    @Override
    public boolean isService() {
        return true;
    }

}
