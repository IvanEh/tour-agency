package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public class PurchaseController extends Controller {

    private PurchaseService purchaseService =
            ServiceLocator.INSTANCE.get(PurchaseService.class);

    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

    @Override
    public void get(RequestService reqService) {
        Long tourId = reqService.getLong("tourId").get();
        Long userId = reqService.getUser().get().getId();
        List<Purchase> purchases = purchaseService.findByUserTour(userId, tourId);
        reqService.putParameter("purchases", purchases);
        reqService.putParameter("tourId", tourId);
        reqService.putParameter("tour", purchases.get(0).getTour());
    }
}
