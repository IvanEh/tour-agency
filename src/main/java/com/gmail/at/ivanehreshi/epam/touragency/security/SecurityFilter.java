package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import org.apache.logging.log4j.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * The filter 'weaves' the SecurityContext constraints into the web application
 * by replacing ServletRequest and ServletResponse with a custom secure one
 *
 * @see SecurityContext
 * @see SecuredHttpServletRequest
 */
@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                new SecuredHttpServletRequest((HttpServletRequest) request);

        User currentUser = getCurrentUser(httpRequest);

        List<Role> roles = Objects.isNull(currentUser) ?
                new ArrayList<>() : currentUser.getRoles();

        if(!checkPermissions(request, response, roles)) {
            return;
        }

        chain.doFilter(httpRequest, response);

    }

    private boolean checkPermissions(ServletRequest request, ServletResponse response, List<Role> roles) throws ServletException, IOException {
        String extraPath = ((HttpServletRequest) request).getRequestURI();

        HttpMethod method = HttpMethod.valueOf(((HttpServletRequest) request).getMethod());

        if(!SecurityContext.INSTANCE.allowed(extraPath, method ,roles)) {
            try {
                request.getRequestDispatcher(SecurityContext.INSTANCE.getLoginPage())
                        .forward(request, response);
            } catch (AccessDeniedException e) {
                LOGGER.info("Someone tried to access a resource without permission", e);
            }

            return false;
        }

        return true;
    }

    @Override
    public void destroy() {

    }

    public User getCurrentUser(HttpServletRequest httpRequest) {
        return (User) httpRequest.getSession(true).getAttribute("user");
    }
}
