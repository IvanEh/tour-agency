package com.gmail.at.ivanehreshi.epam.touragency.web;

import com.gmail.at.ivanehreshi.epam.touragency.controller.*;
import com.gmail.at.ivanehreshi.epam.touragency.controller.service.JspController;
import com.gmail.at.ivanehreshi.epam.touragency.controller.service.LocaleController;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.ControllerDispatcherServletBuilder;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.HttpMethod;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.JdbcTemplate;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.PurchaseJdbcDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.TourJdbcDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.UserJdbcDao;
import com.gmail.at.ivanehreshi.epam.touragency.security.SecurityContext;
import com.gmail.at.ivanehreshi.epam.touragency.util.ResourcesUtil;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

import javax.servlet.ServletContext;
import java.io.File;

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
        ServiceLocator.INSTANCE.setServletContext(servletContext);
        serviceLocator = ServiceLocator.INSTANCE;

        createDb();

        TourDao tourDao = new TourJdbcDao(connectionManager);
        UserDao userDao = new UserJdbcDao(connectionManager);

        serviceLocator.publish(TourDao.class, tourDao);
        serviceLocator.publish(UserDao.class, userDao);
        serviceLocator.publish(PurchaseDao.class, new PurchaseJdbcDao(connectionManager, userDao, tourDao));

        SecurityContext.INSTANCE.setUserDao(userDao);

        configureSecurity(SecurityContext.INSTANCE);

        ControllerDispatcherServletBuilder servletBuilder = new ControllerDispatcherServletBuilder(servletContext);
        buildDispatcherServlet(servletBuilder)
                .buildAndRegister("Command Dispatcher Servlet", "/app/*");
    }

    private void configureSecurity(SecurityContext sc) {
        sc.addSecurityConstraint("/agent/.*", Role.TOUR_AGENT)
                .addSecurityConstraint("/user/.*");
    }

    private ControllerDispatcherServletBuilder buildDispatcherServlet(ControllerDispatcherServletBuilder servletBuilder) {
        return servletBuilder
                .addMapping("/", new RedirectController("/index.html"))
                .addMapping("/tours", new ToursController())
                .addMapping("/tours\\.html", HttpMethod.GET.mask, new ToursController())
                .addMapping("/user/buy\\.html", new BuyController())
                .addMapping("/agent/tours\\.html", new AgentToursPageController())
                .addMapping("/agent/users\\.html", new AgentUsersPageController())
                .addMapping("/index\\.html", new RandomHotTourController())
                .addMapping("/register", new RegisterController())
                .addMapping("/user/discount", new UpdateDiscountController())
                .addMapping("/purchase", new PurchaseController())
                .addMapping("/login", new LoginController())
                .addMapping("/logout", new LogoutController())
                .addMapping("/user/purchases\\.html", new PurchaseController())
                .addMapping("/(.*)\\.html", new JspController("/pages/", ".html"))
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
