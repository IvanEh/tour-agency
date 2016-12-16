package com.gmail.at.ivanehreshi.epam.touragency.web;

import com.gmail.at.ivanehreshi.epam.touragency.command.*;
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
import com.gmail.at.ivanehreshi.epam.touragency.servlet.CommandDispatcherServletBuilder;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.HttpMethod;
import com.gmail.at.ivanehreshi.epam.touragency.util.ResourcesUtil;

import javax.servlet.ServletContext;
import java.io.File;

public enum WebApplication {
    INSTANCE;

    private ServletContext servletContext;

    private ConnectionManager connectionManager;

    private ServiceLocator serviceLocator;

    WebApplication() {
        connectionManager = new ConnectionManager();
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
        SecurityContext.INSTANCE.addSecurityConstraint("/agent/.*", Role.TOUR_AGENT)
                                .addSecurityConstraint("/user/.*");

        CommandDispatcherServletBuilder servletBuilder = new CommandDispatcherServletBuilder(servletContext);
        servletBuilder
                      .addMapping("/", new RedirectController("/index.html"))
                      .addMapping("/tours", new ToursController())
                      .addMapping("/tours\\.html", HttpMethod.GET.mask, new ToursController())
                      .addMapping("/user/buy\\.html", new BuyController())
                      .addMapping("/agent/tours\\.html", new AgentToursPageController())
                      .addMapping("/register", new RegisterController())
                      .addMapping("/user/discount", new UpdateDiscountController())
                      .addMapping("/purchase", new PurchaseController() )
                      .addMapping("/login", new LoginController())
                      .addMapping("/logout", new LogoutController())
                      .addMapping("/user/purchases\\.html", new PurchaseController())
                      .addMapping("/(.*)\\.html", new JspController("/pages/", ".html"))
                      .buildAndRegister("Command Dispatcher Servlet", "/app/*");
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
