package com.gmail.at.ivanehreshi.epam.touragency.web;

import com.gmail.at.ivanehreshi.epam.touragency.controller.*;
import com.gmail.at.ivanehreshi.epam.touragency.controller.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;
import com.gmail.at.ivanehreshi.epam.touragency.security.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.impl.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import javax.servlet.*;
import java.io.*;

/**
 * The web application itself. It has two goals:
 * 1. configure the application(connection manager, service locator, security, controllers)
 * 2. bootstrap the application
 */
public enum WebApplication {
    INSTANCE;

    private ServletContext servletContext;

    private ConnectionManager connectionManager;

    private ServiceLocator serviceLocator;

    WebApplication() {
        connectionManager = ConnectionManager.fromJndi("jdbc/tour_agency");
    }

    protected void init() {
        serviceLocator = ServiceLocator.INSTANCE;

        createDb();

        TourDao tourDao = new TourJdbcDao(connectionManager);
        UserDao userDao = new UserJdbcDao(connectionManager);
        PurchaseDao purchaseDao = new PurchaseJdbcDao(connectionManager, userDao, tourDao);
        ReviewDao reviewDao = new ReviewJdbcDao(connectionManager);

        UserService userService = new UserServiceImpl(userDao);
        TourService tourService = new TourServiceImpl(tourDao);
        PurchaseService purchaseService = new PurchaseServiceImpl(purchaseDao, tourDao,
                userDao);
        AuthService authService = new AuthServiceImpl(userService);
        ReviewService reviewService = new ReviewServiceImpl(reviewDao, userDao);

        serviceLocator.publish(tourDao, TourDao.class);
        serviceLocator.publish(userDao, UserDao.class);
        serviceLocator.publish(purchaseDao, PurchaseDao.class);;

        serviceLocator.publish(userService, UserService.class);
        serviceLocator.publish(tourService, TourService.class);
        serviceLocator.publish(purchaseService, PurchaseService.class);
        serviceLocator.publish(authService, AuthService.class);
        serviceLocator.publish(reviewService, ReviewService.class);

        SecurityContext.INSTANCE.setUserDao(userDao);

        configureSecurity(SecurityContext.INSTANCE);

        ControllerDispatcherServletBuilder servletBuilder = new ControllerDispatcherServletBuilder(servletContext);
        buildDispatcherServlet(servletBuilder)
                .buildAndRegister("Command Dispatcher Servlet", "/app/*");
    }

    private void configureSecurity(SecurityContext sc) {
        sc.addSecurityConstraint("/agent/.*", Role.TOUR_AGENT)
                .addSecurityConstraint("/user/.*", Role.CUSTOMER, Role.TOUR_AGENT);
    }

    private ControllerDispatcherServletBuilder buildDispatcherServlet(ControllerDispatcherServletBuilder servletBuilder) {
        return servletBuilder
                .addMapping("/", new RedirectController("/index.html"))
                .addMapping("/tours", new ToursController())
                .addMapping("/tours\\.html", HttpMethod.GET.mask, new ToursController())
                .addMapping("/tour\\.html", new TourController())
                .addMapping("/user/buy\\.html", new BuyController())
                .addMapping("/agent/tours\\.html", new AgentToursPageController())
                .addMapping("/agent/users\\.html", new AgentUsersPageController())
                .addMapping("/index\\.html", new RandomHotTourController())
                .addMapping("/register", new RegisterController())
                .addMapping("/user/discount", new UpdateDiscountController())
                .addMapping("/purchase", new PurchaseController())
                .addMapping("/login", new LoginController())
                .addMapping("/logout", new LogoutController())
                .addMapping("/review", new ReviewController())
                .addMapping("/user/purchases\\.html", new PurchaseController())
                .addMapping("/agent/new-tour\\.html", new AgentNewTourPageController())
                .addMapping("/agent/edit-tour\\.html", new AgentEditTourController())
                .addMapping("/(.*)\\.html", new JspController("/pages/", ".html", ".html"))
                .addMapping("/(.*)\\.html", new LocaleController());
    }

    private void createDb() {
        File file = ResourcesUtil.getResourceFile("database.sql");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionManager);
        jdbcTemplate.executeSqlFile(file);
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

}
