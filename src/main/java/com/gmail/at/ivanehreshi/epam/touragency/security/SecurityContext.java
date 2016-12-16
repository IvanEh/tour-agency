package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum  SecurityContext {
    INSTANCE;

    private static Logger LOGGER = LogManager.getLogger(SecurityContext.class);

    private UserDao userDao;

    private String loginPage = "/login.html";

    private List<SecurityConstraint> securityConstraints = new ArrayList<>();

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public SecurityContext addSecurityConstraint(String s, Role... roles) {
        rwLock.writeLock().lock();
        try {
            securityConstraints.add(new SecurityConstraint(s, roles));
            return this;

        } finally {
            rwLock.writeLock().unlock();
        }
    }

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
            if(rolesAllowed.isEmpty()) {
                return !roles.isEmpty();
            }

            for(Role role: roles) {
                if(rolesAllowed.contains(role)) {
                    return true;
                }
            }

            return false;
        }
    }
}
