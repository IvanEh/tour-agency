package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RequestService {
    private static Logger LOGGER = LogManager.getLogger(RequestService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<String> groups;
    private String pagePath = null;

    public RequestService(HttpServletRequest request, HttpServletResponse response, List<String> groups) {
        this.request = request;
        this.response = response;
        this.groups = groups;
    }

    public void putParameter(String key, Object o) {
        request.setAttribute(key, o);
    }

    public void putParameter(Object o) {
        request.setAttribute(o.getClass().getSimpleName(), o);
    }

    public void redirect(String where) {
        try {
            response.sendRedirect(where);
        } catch (IOException e) {
            LOGGER.error("Cannot redirect in controller", e);
        }
    }

    public void renderPage(String path) {
        this.pagePath = path;
    }

    public void setStatus(int status) {
        response.setStatus(status);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public List<String> getPathParameters() {
        return groups;
    }

    public String getPathParameter(int i) {
        return i >= groups.size() ? groups.get(i) : "";
    }

    String getRenderPage() {
        return pagePath;
    }

    public Long getLong(String parameter) {
        return Long.valueOf(request.getParameter(parameter));
    }

    public String getString(String parameter) {
        return request.getParameter(parameter);
    }

    public User getUser() {
        return (User) request.getSession(false).getAttribute("user");
    }
}
