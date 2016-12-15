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
        User user = getCurrentUser();
        if(user == null) {
            return false;
        }

        if (user.getRoles().stream().map(Enum::name).anyMatch(s -> s.equals(role))) {
            return true;
        }

        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {
        User user;

        user = securityContext.getUserDao().read(username);

        if (user != null && user.getPassword().equals(PasswordEncoder.encodePassword(password))) {
            getHttpRequest().getSession(true).setAttribute("user", user);
            getHttpRequest().getSession(false).setAttribute("loggedIn", true);
        } else {
            getHttpRequest().getSession().invalidate();
            throw new ServletException("Bad login credentials");
        }
    }

    @Override
    public void logout() throws ServletException {
        getSession().invalidate();
    }

    public User getCurrentUser() {
        return (User) getSession(true).getAttribute("user");
    }
}
