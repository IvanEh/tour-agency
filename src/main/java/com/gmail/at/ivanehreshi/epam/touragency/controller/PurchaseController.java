package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public final class PurchaseController extends Controller {

    private PurchaseService purchaseService =
            ServiceLocator.INSTANCE.get(PurchaseService.class);


    @Override
    public void get(RequestService reqService) {
        User user = reqService.loadUser().orElse(null);

        List<Purchase> purchases = purchaseService.findByUser(user.getId());

        reqService.putParameter("purchases", purchases);
    }

    @Override
    public void post(RequestService reqService) {
        Long tourId = reqService.getLong("tourId").orElse(null);
        Long userId = reqService.getUser().orElse(null).getId();

        purchaseService.purchase(userId, tourId);

        reqService.redirect("/user/purchases.html");
    }
}
