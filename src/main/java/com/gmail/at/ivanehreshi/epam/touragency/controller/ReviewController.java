package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.security.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public class ReviewController extends Controller {

    private ReviewService reviewService = ServiceLocator.INSTANCE.get(ReviewService.class);

    @Override
    public void post(RequestService reqService) {
        Review review = new Review();
        review.setRating(reqService.getInt("rating").get());
        review.setTour(reqService.getLong("tourId").map(Tour::new).get());
        review.setAuthor(reqService.getUser().get());
        review.setText(reqService.getString("text"));

        reviewService.create(review);

        reqService.redirect(reqService.getRequest().getHeader("Referer"));
    }

    @Override
    public void put(RequestService reqService) {
        Review review = new Review();
        review.setId(reqService.getLong("id").get());
        review.setRating(reqService.getInt("rating").get());
        review.setTour(reqService.getLong("tourId").map(Tour::new).get());
        review.setAuthor(reqService.getUser().get());
        review.setText(reqService.getString("text"));

        reviewService.update(review);

        reqService.redirect(reqService.getRequest().getHeader("Referer"));
    }

    @Override
    public void delete(RequestService reqService) {
        Long id = reqService.getLong("id").get();
        Review review = reviewService.read(id);
        User user = reqService.getUser().get();
        if (!review.getAuthor().getId().equals(user.getId())
                && !reqService.getRequest().isUserInRole(Role.TOUR_AGENT.name())) {
            throw new AccessDeniedException();
        }
        reviewService.delete(id);
        reqService.redirect(reqService.getRequest().getHeader("Referer"));
    }
}
