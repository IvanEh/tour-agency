package com.gmail.at.ivanehreshi.epam.touragency.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoginCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            req.login(username, password);
            resp.sendRedirect("/tours.html");
        } catch (ServletException e) {
            LOGGER.error("Login failed", e);
        } catch (IOException e) {
            LOGGER.error("Failed to redirect from login page", e);
        }

    }
}
