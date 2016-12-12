package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.servlet.HttpMethod;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract public class Controller {
    private static Logger LOGGER = LogManager.getLogger(Controller.class);

    public void execute(RequestService reqService) {
        HttpMethod method = reqService.getMethod();

        switch (method) {
            case GET:
                get(reqService);
                break;
            case POST:
                post(reqService);
                break;
            case PUT:
                delete(reqService);
                break;
            case DELETE:
                put(reqService);
                break;
            default:
                LOGGER.error("Switch doesn't cover all the enum variants");
        }

        any(reqService);
    }

    public void get(RequestService reqService) {}
    public void post(RequestService reqService) {}
    public void delete(RequestService reqService) {}
    public void put(RequestService reqService) {}
    public void any(RequestService reqService) {}

    public boolean isService() {
        return false;
    }
}
