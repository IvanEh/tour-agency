package com.gmail.at.ivanehreshi.epam.touragency.dispatcher;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.security.SecurityContext;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;
import com.gmail.at.ivanehreshi.epam.touragency.util.TryOptionalUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A wrapper class that contains all the necessary information for
 * processing a request by {@link Controller} along with some
 * helper methods
 */
public class RequestService {
    private static Logger LOGGER = LogManager.getLogger(RequestService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<String> groups;
    private String pagePath = null;
    private String redirectPath = null;
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);

    public RequestService(HttpServletRequest request, HttpServletResponse response,
                          List<String> groups) {
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

    public Optional<String> getParameter(String parameter) {
        String s = request.getParameter(parameter);
        return Optional.ofNullable(s).filter(v -> !v.isEmpty());
    }

    public Optional<Long> getLong(String param) {
        return getParameter(param)
                .flatMap((s) -> TryOptionalUtil.of(() -> Long.valueOf(s)));
    }

    public Optional<Integer> getInt(String param) {
        return getParameter(param)
                .flatMap((s) -> TryOptionalUtil.of(() -> Integer.valueOf(s)));
    }

    /**
     * Retrieves <b>param</b> request parameter as a non null String
     * @param param
     * @return a String representation of the request parameter or an empty string
     */
    public String getString(String param) {
        return getParameter(param).orElse("");
    }

    public Optional<Boolean> getBool(String param) {
        return getParameter(param)
                .flatMap((s) -> TryOptionalUtil.of(() -> s.equals("1") || s.equals(true)));
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

    public Optional<User> getUser() {
        return SecurityContext.INSTANCE.getCurrentUser(request);
    }

    public Optional<User> loadUser() {
        SecurityContext.INSTANCE.updateUserCache(request);
        return SecurityContext.INSTANCE.getCurrentUser(request);
    }

    public boolean isRedirect() {
        Boolean b = (Boolean) request.getSession().getAttribute(ControllerDispatcherServlet.REDIRECT_KEY);

        return b != null && b;
    }

    public void clearRedirectFlag() {
        request.getSession().setAttribute(ControllerDispatcherServlet.REDIRECT_KEY, null);
    }

    // TODO: what if  __method=rubbish ???
    public HttpMethod getMethod() {
        String formMethod = getString("__method");

        try {
            if (formMethod != null)
                return HttpMethod.valueOf(formMethod.toUpperCase());
        } catch (IllegalArgumentException e) { }

        return HttpMethod.valueOf(request.getMethod());
    }

    public void putFlashParameter(String param, Object o) {
        Map<String, Object> flash =
                (Map<String, Object>) request.getSession().getAttribute(ControllerDispatcherServlet.FLASH_SESSION_KEY);
        if(flash == null) {
            flash = new HashMap<>();
        }

        flash.put(param, o);

        request.getSession().setAttribute(ControllerDispatcherServlet.FLASH_SESSION_KEY, flash);
    }

    public Object getFlashParameter(String param) {
        Map<String, Object> flash =
                (Map<String, Object>) request.getSession().getAttribute(ControllerDispatcherServlet.FLASH_SESSION_KEY);
        if(flash == null) {
            return null;
        }

        return flash.get(param);
    }

    public void clearFlash() {
        Map<String, Object> flash =
                (Map<String, Object>) request.getSession().getAttribute(ControllerDispatcherServlet.FLASH_SESSION_KEY);

        if(flash == null) {
            flash = new HashMap<>();
        }

        flash.clear();

        request.getSession().setAttribute(ControllerDispatcherServlet.FLASH_SESSION_KEY, flash);
    }
}
