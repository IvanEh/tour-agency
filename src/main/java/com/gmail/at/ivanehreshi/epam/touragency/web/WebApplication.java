package com.gmail.at.ivanehreshi.epam.touragency.web;

import com.gmail.at.ivanehreshi.epam.touragency.controller.*;
import com.gmail.at.ivanehreshi.epam.touragency.controller.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.factory.*;
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

    private DaoFactory daoFactory;

    WebApplication() {
        connectionManager = ConnectionManager.fromJndi("jdbc/tour_agency");
    }

    protected void init() {
        serviceLocator = ServiceLocator.INSTANCE;

        createDb();

        daoFactory = new JdbcDaoFactory(connectionManager);

        UserService userService = new UserServiceImpl(daoFactory.getUserDao());

        TourService tourService = new TourServiceImpl(daoFactory.getTourDao());

        PurchaseService purchaseService = new PurchaseServiceImpl(daoFactory.getPurchaseDao(),
                        daoFactory.getTourDao(), daoFactory.getUserDao());

        AuthService authService = new AuthServiceImpl(userService);

        ReviewService reviewService = new ReviewServiceImpl(daoFactory.getReviewDao(),
                daoFactory.getUserDao());

        TourImageService tourImageService = new TourImageServiceImpl(daoFactory.getTourImageDao());

        serviceLocator.publish(userService, UserService.class);
        serviceLocator.publish(tourService, TourService.class);
        serviceLocator.publish(purchaseService, PurchaseService.class);
        serviceLocator.publish(authService, AuthService.class);
        serviceLocator.publish(reviewService, ReviewService.class);
        serviceLocator.publish(tourImageService, TourImageService.class);
        serviceLocator.publish(daoFactory.getTourDao(), TourDao.class);

        SecurityContext.INSTANCE.setUserDao(daoFactory.getUserDao());

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
                .addMapping("/tours", HttpMethod.GET.single(), new ToursController())
                .addMapping("/tours", HttpMethod.modifying(), new ToursController())
                .addMapping("/tours\\.html", HttpMethod.GET.single(), new ToursController())
                .addMapping("/tour\\.html", new TourController())
                .addMapping("/user/buy\\.html", new BuyController())
                .addMapping("/agent/tours\\.html", new AgentToursPageController())
                .addMapping("/agent/users\\.html", new AgentUsersPageController())
                .addMapping("/index\\.html", new RandomHotTourController())
                .addMapping("/register", new RegisterController())
                .addMapping("/user/discount", HttpMethod.any(),
                        new UpdateDiscountController(), Role.TOUR_AGENT)
                .addMapping("/purchase", HttpMethod.any(), new PurchaseController(),
                        Role.CUSTOMER, Role.TOUR_AGENT)
                .addMapping("/login", new LoginController())
                .addMapping("/logout", new LogoutController())
                .addMapping("/review", new ReviewController())
                .addMapping("/lang", new LocaleController())
                .addMapping("/tour-images", HttpMethod.GET.single() ,new TourImagesController())
                .addMapping("/tour-images", HttpMethod.modifying(),
                        new TourImagesController(), Role.TOUR_AGENT)
                .addMapping("/login\\.html", HttpMethod.GET.single(), new LoginController())
                .addMapping("/register\\.html", HttpMethod.GET.single(), new RegisterController())
                .addMapping("/user/purchases\\.html", new PurchaseController())
                .addMapping("/agent/new-tour\\.html", new AgentNewTourPageController())
                .addMapping("/agent/edit-tour\\.html", new AgentEditTourController())
                .addMapping("/(.*)\\.html", new JspController("/pages/", ".html", ".html"));
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
