package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import java.math.BigDecimal;

public class ToursController extends Controller {
    @Override
    public void post(RequestService reqService) {
        Tour tour = new Tour();
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setPrice(new BigDecimal(reqService.getString("price")));
        tour.setType(TourType.values()[reqService.getInt("type")]);
        tour.setHot(reqService.getBool(""));
        WebApplication.INSTANCE.getTourDao().create(tour);

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

        WebApplication.INSTANCE.getTourDao().update(tour);

        reqService.redirect("/agent/tours.html");
    }
}
