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
        SecurityContext.INSTANCE.addSecurityConstraint("/agent/.*", Role.TOUR_AGENT)
                                .addSecurityConstraint("/buy.html")
                                .addSecurityConstraint("/purchases.html");

        CommandDispatcherServletBuilder servletBuilder = new CommandDispatcherServletBuilder(servletContext);
        servletBuilder
                      .mapPost("/tour", new CreateTourCommand())
                      .mapPost("/tour/edit", new EditTourCommand())
                      .mapPost("/register", new RegisterCommand())
                      .mapPost("/user/discount", new UpdateDiscountCommand())
                      .mapPost("/purchase", new PurchaseCommand())
                      .mapPost("/login", new LoginCommand())
                      .mapPost("/logout", new LogoutCommand())
                      .mapGet("/purchases\\.html", new PurchaseController())
                      .mapGet("/(.*)\\.html", new JspController("/pages/", ".jsp"))
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
