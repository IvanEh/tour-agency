package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.ObjectFactory;

public class BuyController extends Controller {
    private TourDao tourDao = ObjectFactory.INSTANCE.get(TourDao.class);

    @Override
    public void get(RequestService reqService) {
        Long tourId = reqService.getLong("tourId");
        Tour tour = tourDao.read(tourId);
        reqService.putParameter("tour", tour);
        reqService.putParameter("price", tourDao.computePrice(tourId, reqService.getUser().getId()));
    }
}
