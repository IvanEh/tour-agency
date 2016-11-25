package com.gmail.at.ivanehreshi.epam.touragency.web;

import com.gmail.at.ivanehreshi.epam.touragency.command.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.PurchaseJdbcDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.TourJdbcDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.UserJdbcDao;
import com.gmail.at.ivanehreshi.epam.touragency.security.SecurityContext;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.CommandDispatcherServletBuilder;

import javax.servlet.ServletContext;

public enum WebApplication {
    INSTANCE;

    private ServletContext servletContext;

    private ConnectionManager connectionManager;

    private TourDao tourDao;

    private UserDao userDao;

    private PurchaseDao purchaseDao;

    WebApplication() {
        connectionManager = new ConnectionManager();
    }

    protected void init() {
        tourDao = new TourJdbcDao(connectionManager);
        userDao = new UserJdbcDao(connectionManager);
        purchaseDao = new PurchaseJdbcDao(connectionManager, userDao, tourDao);

        SecurityContext.INSTANCE.setUserDao(userDao);
        SecurityContext.INSTANCE.addSecurityConstraint("/agent/.*", Role.CUSTOMER);

        CommandDispatcherServletBuilder servletBuilder = new CommandDispatcherServletBuilder(servletContext);
        servletBuilder.addMapping("/tour", new CreateTourCommand())
                      .addMapping("/tour/edit", new EditTourCommand())
                      .addMapping("/register", new RegisterCommand())
                      .addMapping("/user/discount", new UpdateDiscountCommand())
                      .addMapping("/purchase", new PurchaseCommand())
                      .addMapping("/login", new LoginCommand())
                      .addMapping("/logout", new LogoutCommand())
                      .buildAndRegister("Command Dispatcher Servlet", "/actions/*");
    }

    public TourDao getTourDao() {
        return tourDao;
    }

    public void setTourDao(TourDao tourDao) {
        this.tourDao = tourDao;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public PurchaseDao getPurchaseDao() {
        return purchaseDao;
    }

    public void setPurchaseDao(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }
}
