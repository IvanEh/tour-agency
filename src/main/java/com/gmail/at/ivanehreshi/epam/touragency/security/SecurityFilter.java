package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = new SecuredHttpServletRequest((HttpServletRequest) request);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String extraPath = httpRequest.getRequestURI();

        User currentUser = getCurrentUser(httpRequest);
        List<Role> roles = Objects.isNull(currentUser) ? new ArrayList<>() : currentUser.getRoles();

        if(!SecurityContext.INSTANCE.allowed(extraPath, roles)) {
            httpResponse.sendRedirect(SecurityContext.INSTANCE.getLoginPage());
            return;
        }

        chain.doFilter(httpRequest, response);

    }

    @Override
    public void destroy() {

    }

    public User getCurrentUser(HttpServletRequest httpRequest) {
        return (User) httpRequest.getSession(true).getAttribute("user");
    }
}
