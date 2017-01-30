package com.gmail.at.ivanehreshi.epam.touragency.controller.support;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.math.*;

public class RequestExtractors {
    public static Review extractReview(RequestService reqService) {
        Review review = new Review();

        review.setId(reqService.getLong("id").orElse(null));
        review.setRating(reqService.getInt("rating").orElse(null));
        review.setTour(reqService.getLong("tourId").map(Tour::new).orElse(null));
        review.setAuthor(reqService.getUser().orElse(null));
        review.setText(reqService.getString("text"));

        return review;
    }

    public static WithStatus<Tour> extractTourWithStatus(RequestService reqService) {
        Tour tour = new Tour();

        tour.setId(reqService.getLong("id").orElse(null));
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setType(TourType.values()[reqService.getInt("type").get()]);
        tour.setHot(reqService.getBool("hot").orElse(false));
        tour.setEnabled(reqService.getBool("enabled").orElse(true));

        try {
            tour.setPrice(new BigDecimal(reqService.getString("price")));
        } catch (NumberFormatException e) {
            return WithStatus.bad(tour);
        }

        return WithStatus.ok(tour);
    }
}
