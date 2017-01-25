package com.gmail.at.ivanehreshi.epam.touragency.security;

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
            httpRequest.getRequestDispatcher(SecurityContext.INSTANCE.getLoginPage())
                    .forward(request, response);
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
