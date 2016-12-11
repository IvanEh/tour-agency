package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

public class RequestService {
    private static Logger LOGGER = LogManager.getLogger(RequestService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<String> groups;
    private String pagePath = null;
    private String redirectPath = null;

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
        redirectPath = where;
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

    public Long getLong(String parameter) {
        return Long.valueOf(request.getParameter(parameter));
    }

    public Integer getInt(String parameter) {
        return Integer.valueOf(request.getParameter(parameter));
    }

    public String getString(String parameter) {
        return request.getParameter(parameter);
    }

    public boolean getBool(String parameter) {
        return Objects.equals(request.getParameter(parameter), "1");
    }
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getRenderPage() {
        return pagePath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public User getUser() {
        return (User) request.getSession(false).getAttribute("user");
    }
}
