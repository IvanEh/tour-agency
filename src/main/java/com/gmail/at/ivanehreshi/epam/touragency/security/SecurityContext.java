package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A singleton object for encapsulating user authorization and authentication
 * Allows adding security constrains to the web application
 */
public enum SecurityContext {
    INSTANCE;

    private static Logger LOGGER = LogManager.getLogger(SecurityContext.class);

    private UserDao userDao;

    private String loginPage = "/login.html";

    private List<SecurityConstraint> securityConstraints = new ArrayList<>();

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * Allows access to URLs denoted by the s regular expression
     * for the given roles and denies for other users
     * @param s     regular expression which tested against the URL
     * @param roles roles that allowed to access the given set of pages;
     *              if null - only authenticated user can access the pages
     * @return  the same SecurityContext
     */
    public SecurityContext addSecurityConstraint(String s, Role... roles) {
        rwLock.writeLock().lock();
        try {
            securityConstraints.add(new SecurityConstraint(s, roles));
            return this;

        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Test if a user with the given roles can access the page
     * @param path  URI of the resource starting with /
     * @param roles roles of the user
     * @return true if user allowed to access the resource, false - otherwise
     */
    public boolean allowed(String path, List<Role> roles) {
        rwLock.readLock().lock();
        try {
            if (roles == null) {
                roles = new ArrayList<>();
            }

            boolean matched = false;

            for (SecurityConstraint sc : securityConstraints) {

                if (sc.matches(path)) {
                    return sc.allowed(roles);
                }
            }

            return true;

        } finally {
            rwLock.readLock().unlock();
        }
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void updateUserCache(HttpServletRequest req) {
        Optional<User> maybeUser = getCurrentUser(req);
        if(maybeUser.isPresent()) {
            Long id = maybeUser.get().getId();
            User user = userDao.read(id);
            req.getSession().setAttribute("user", user);
        }
    }

    public Optional<User> getCurrentUser(HttpServletRequest req) {
        return Optional.ofNullable((User) req.getSession(true).getAttribute("user"));
    }

    /**
     * Represents a pair of regular expression and roles allowed to access the
     * set of pages(denoted by the expression)
     */
    private static class SecurityConstraint {
        private final Pattern pattern;
        private final Matcher matcher;
        private final List<Role> rolesAllowed = new ArrayList<>();

        public SecurityConstraint(String pathPattern, Role... rolesAllowed) {
            pattern = Pattern.compile("^" + pathPattern + "$");
            matcher = pattern.matcher("");
            this.rolesAllowed.addAll(Arrays.asList(rolesAllowed));
        }

        public boolean matches(String path) {
            matcher.reset(path);
            return matcher.matches();
        }

        public boolean allowed(List<Role> roles) {
            if (rolesAllowed.isEmpty()) {
                return !roles.isEmpty();
            }

            for (Role role : roles) {
                if (rolesAllowed.contains(role)) {
                    return true;
                }
            }

            return false;
        }
    }
}
