package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.util.PasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class SecuredHttpServletRequest extends HttpServletRequestWrapper {
    private static final Logger LOGGER = LogManager.getLogger(SecuredHttpServletRequest.class);

    private SecurityContext securityContext = SecurityContext.INSTANCE;

    SecuredHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) getRequest();
    }

    @Override
    public Principal getUserPrincipal() {
        User user = (User) getHttpRequest().getSession(false).getAttribute("user");
        return new UserPrincipal(user.getId().toString());
    }

    @Override
    public boolean isUserInRole(String role) {
        return super.isUserInRole(role);
    }

    @Override
    public void login(String username, String password) throws ServletException {
        User user = securityContext.getUserDao().read(username);
        if(user.getPassword().equals(PasswordEncoder.encodePassword(password))) {
            getHttpRequest().getSession(true).setAttribute("user", user);
            LOGGER.error("Login succeeded");
        } else {
            getHttpRequest().getSession().invalidate();
            LOGGER.error("Login failed");
        }
    }

    @Override
    public void logout() throws ServletException {
        getSession().invalidate();
    }
}
