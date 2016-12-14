package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.ObjectFactory;

import java.math.BigDecimal;
import java.util.List;

public class ToursController extends Controller {
    private TourDao tourDao = ObjectFactory.INSTANCE.get(TourDao.class);

    @Override
    public void get(RequestService reqService) {
        List<Tour> tours = tourDao.findAll();
        reqService.putParameter("tours", tours);
    }

    @Override
    public void post(RequestService reqService) {
        Tour tour = new Tour();
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setPrice(new BigDecimal(reqService.getString("price")));
        tour.setType(TourType.values()[reqService.getInt("type")]);
        tour.setHot(reqService.getBool(""));
        tourDao.create(tour);

        reqService.redirect("/agent/tours.html");
    }

    @Override
    public void put(RequestService reqService) {
        Tour tour = new Tour();
        tour.setId(reqService.getLong("id"));
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setPrice(new BigDecimal(reqService.getString("price")));
        tour.setType(TourType.values()[reqService.getInt("type")]);
        tour.setHot(reqService.getBool("hot"));

        tourDao.update(tour);

        reqService.redirect("/agent/tours.html");
    }
}
