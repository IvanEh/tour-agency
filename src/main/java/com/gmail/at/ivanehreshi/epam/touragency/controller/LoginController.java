package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;

public class LoginController extends Controller {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Override
    public void post(RequestService reqService) {
        String username = reqService.getString("username");
        String password = reqService.getString("password");

        try {
            reqService.getRequest().login(username, password);

            reqService.redirect("/tours.html");
        } catch (ServletException e) {
            reqService.redirect("/login.html?failed=true");
        }
    }
}
