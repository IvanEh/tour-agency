package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public class TourController extends Controller {

    private TourService tourService =
            ServiceLocator.INSTANCE.get(TourService.class);

    private ReviewService reviewService =
            ServiceLocator.INSTANCE.get(ReviewService.class);

    private TourImageService tourImageService =
            ServiceLocator.INSTANCE.get(TourImageService.class);

    @Override
    public void get(RequestService reqService) {
        Long id = reqService.getLong("id").get();

        Tour tour = tourService.read(id);
        if(tour.getAvgRating() == null)
            tour.setAvgRating(0.0);

        List<Review> reviews = reviewService.findByTour(id);
        List<TourImage> tourImages = tourImageService.findByTour(tour.getId());

        reqService.setPageAttribute("tour", tour);

        reqService.setPageAttribute("agent",
                reqService.getRequest().isUserInRole(Role.TOUR_AGENT.name()));

        reqService.setPageAttribute("discount", Math.max(reqService.loadUser()
                .map(User::getDiscount)
                .orElse(0), tour.getDiscount()));

        reqService.setPageAttribute("canVote", reviewService.canVote(reqService.getUser()
                .map(User::getId).orElse(null), id));

        reqService.setPageAttribute("reviews", reviews);
        reqService.setPageAttribute("tourImages", tourImages);
        Review oldReview = reviewService.findByPurchase(
                reqService.getUser().map(User::getId).orElse(null), tour.getId());
        reqService.setPageAttribute("oldReview", oldReview);
    }
}
