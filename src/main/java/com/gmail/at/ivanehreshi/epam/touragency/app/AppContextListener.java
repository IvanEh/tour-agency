package com.gmail.at.ivanehreshi.epam.touragency.app;

import org.apache.logging.log4j.*;

import javax.servlet.*;
import javax.servlet.annotation.*;

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
