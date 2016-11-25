package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;

public enum  SecurityContext {
    INSTANCE;

    private UserDao userDao;

    private String loginPage;

    private String errorPage;

    private String registrationPage;

    private String logoutPage;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getRegistrationPage() {
        return registrationPage;
    }

    public void setRegistrationPage(String registrationPage) {
        this.registrationPage = registrationPage;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getLogoutPage() {
        return logoutPage;
    }
}
