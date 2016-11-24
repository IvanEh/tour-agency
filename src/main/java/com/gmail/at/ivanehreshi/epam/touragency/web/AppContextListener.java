package com.gmail.at.ivanehreshi.epam.touragency.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger LOGGER
            = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("webApplication", WebApplication.INSTANCE);
        LOGGER.info("WebApplication initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
