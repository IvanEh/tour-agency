package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import org.apache.logging.log4j.*;

import javax.servlet.*;

public final class LogoutController extends Controller {
    private static final Logger LOGGER = LogManager.getLogger(LogoutController.class);

    @Override
    public void post(RequestService reqService) {
        try {
            reqService.getRequest().logout();
        } catch (ServletException e) {
            LOGGER.error("Error occurred when user tried to logout", e);
        }

        reqService.redirect("/login.html");

    }
}
