package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

public final class BuyController extends Controller {
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    @Override
    public void get(RequestService reqService) {
        Long tourId = reqService.getLong("tourId");
        Tour tour = tourDao.read(tourId);
        Long userId = reqService.getUser().getId();

        reqService.putParameter("tour", tour);
        reqService.putParameter("price", tourDao.computePrice(tourId, userId));
    }
}
