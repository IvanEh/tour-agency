package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public final class BuyController extends Controller {
    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

    private UserService userService = ServiceLocator.INSTANCE.get(UserService.class);

    @Override
    public void get(RequestService reqService) {
        Long tourId = reqService.getLong("tourId").get();
        Tour tour = tourService.read(tourId);
        Long userId = reqService.getUser().get().getId();

        reqService.putParameter("tour", tour);
        reqService.putParameter("discount", userService.computeDiscount(userId, tourId));
        reqService.putParameter("price", tourService.computePrice(tourId, userId));
    }
}
