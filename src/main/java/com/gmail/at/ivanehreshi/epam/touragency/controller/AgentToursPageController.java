package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

import java.util.List;

public class AgentToursPageController extends Controller {
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    @Override
    public void get(RequestService reqService) {
        List<Tour> tours = tourDao.findAll();
        reqService.putParameter("tours", tours);
    }

    @Override
    public void post(RequestService reqService) {
        String command = reqService.getString("command");
        Long id = reqService.getLong("id");
        System.out.println("comm:" + command);
        switch (command) {
            case "toggle":
                Tour tour = tourDao.read(id);
                tour.setEnabled(!tour.isEnabled());
                tourDao.update(tour);
                break;
        }
    }
}
