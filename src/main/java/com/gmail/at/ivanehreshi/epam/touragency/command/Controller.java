package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract public class Controller {
    private static Logger LOGGER = LogManager.getLogger(Controller.class);

    public void execute(RequestService reqService) {

        switch (reqService.getRequest().getMethod()) {
            case "GET":
                get(reqService);
                break;
            case "POST":
                post(reqService);
                break;
            case "DELETE":
                delete(reqService);
                break;
            default:
        }

        any(reqService);
    }

    public void get(RequestService reqService) {}
    public void post(RequestService reqService) {}
    public void delete(RequestService reqService) {}
    public void any(RequestService reqService) {}

    public boolean isService() {
        return false;
    }
}
