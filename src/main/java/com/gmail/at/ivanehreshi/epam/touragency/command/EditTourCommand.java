package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class EditTourCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(EditTourCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
        Tour tour = new Tour();
        tour.setId(Long.valueOf(req.getParameter("id")));
        tour.setTitle(req.getParameter("title"));
        tour.setDescription(req.getParameter("description"));
        tour.setPrice(new BigDecimal(req.getParameter("price")));
        tour.setType(TourType.values()[Integer.valueOf(req.getParameter("type"))]);
        tour.setHot(Objects.equals(req.getParameter("hot"), "1"));
        WebApplication.INSTANCE.getTourDao().update(tour);

        try {
            resp.sendRedirect("/agent/tours.html");
        } catch (IOException e) {
            LOGGER.error("Error redirecting response", e);
        }
    }
}
