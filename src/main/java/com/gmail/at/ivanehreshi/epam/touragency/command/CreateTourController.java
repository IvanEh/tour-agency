package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class CreateTourController extends Controller {
    private static final Logger LOGGER = LogManager.getLogger(CreateTourController.class);

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
}
