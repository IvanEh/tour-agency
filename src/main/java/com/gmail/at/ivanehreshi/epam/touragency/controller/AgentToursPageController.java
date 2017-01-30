package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.apache.logging.log4j.*;

import java.util.*;

public final class AgentToursPageController extends Controller {

    private static final Logger LOGGER = LogManager.getLogger(AgentToursPageController.class);

    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

    @Override
    public void get(RequestService reqService) {
        List<Tour> tours = tourService.findAll();
        Collections.reverse(tours);
        reqService.putParameter("tours", tours);
    }

    @Override
    public void post(RequestService reqService) {
        String command = reqService.getString("command");
        Long id = reqService.getLong("id").orElse(null);

        switch (command) {
            case "toggle":
                tourService.toggleEnabled(id);
                break;
            default:
                LOGGER.info("Default branch should not be reached");
        }
    }
}
