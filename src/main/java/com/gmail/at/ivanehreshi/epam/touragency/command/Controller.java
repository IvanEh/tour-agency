package com.gmail.at.ivanehreshi.epam.touragency.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

abstract public class Controller implements Command {
    private static Logger LOGGER = LogManager.getLogger(Controller.class);


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
        RequestService requestService = new RequestService(req, resp, groups);

        switch (req.getMethod()) {
            case "GET":
                get(requestService);
                break;
            case "POST":
                post(requestService);
                break;
            case "DELETE":
                delete(requestService);
                break;
            default:
        }

        any(requestService);

        if(requestService.getRenderPage() != null) {
            try {
                req.getRequestDispatcher(requestService.getRenderPage()).forward(req, resp);
            } catch (ServletException | IOException e) {
                LOGGER.error("Cannot render page", e);
            }
        }

    }

    public void get(RequestService service) {}
    public void post(RequestService service) {}
    public void delete(RequestService service) {}
    public void any(RequestService service) {}

}
