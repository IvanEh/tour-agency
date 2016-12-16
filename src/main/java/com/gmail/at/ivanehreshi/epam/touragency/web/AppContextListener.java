package com.gmail.at.ivanehreshi.epam.touragency.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Entry point of the web application
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger LOGGER
            = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("webApplication", WebApplication.INSTANCE);
        WebApplication.INSTANCE.setServletContext(sce.getServletContext());
        WebApplication.INSTANCE.init();

        LOGGER.info("WebApplication initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
