package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class AgentToursPageController extends Controller {
    private static final Logger LOGGER = LogManager.getLogger(AgentToursPageController.class);

    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    @Override
    public void get(RequestService reqService) {
        List<Tour> tours = tourDao.findAll();
        reqService.putParameter("tours", tours);
    }

    @Override
    public void post(RequestService reqService) {
        String command = reqService.getString("command");
        Long id = reqService.getLong("id").orElse(null);

        switch (command) {
            case "toggle":
                Tour tour = tourDao.read(id);
                tour.setEnabled(!tour.isEnabled());
                tourDao.update(tour);
                break;
            default:
                LOGGER.info("Default branch should not be reached");
        }
    }
}
